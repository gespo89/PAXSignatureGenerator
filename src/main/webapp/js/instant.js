const BADGE_WIDTH = 23;
const BADGE_HEIGHT = 29;
const NUM_WIDTHS = [9,5,9,9,9,9,9,9,9,9];
const NUM_OFFSET = BADGE_WIDTH * 2;
const NUM_HEIGHT = 11;
const TEXT_OFFSET_Y = 11;
const SPECIAL_TEXT_OFFSET_Y = 7;
const RIBBON_HEIGHT = 7
const RIBBON_WIDTH = BADGE_WIDTH
const BADGE_TYPES = {"ATTENDEE": {value: 0, name: "Attendee"},
                     "BYOC": {value: 1, name:"BYOC"},
                     "MEDIA": {value: 2, name: "Media"},
                     "OMEGANAUT": {value: 3, name: "Omeganaut"},
                     "ENFORCER": {value: 4, name: "Enforcer"}}
const BADGE_BASE  = {"ATTENDEE": {value: 0},
                     "ENFORCER": {value: 1}}

const COLOR_WHITE = Jimp.rgbaToInt(255, 255, 255, 255)
const COLOR_BLACK = Jimp.rgbaToInt(0, 0, 0, 255)
const BADGE_BASE_DATA = [
  { name: "Attendee", color: "PAX", textcolor: COLOR_WHITE },
  { name: "Enforcer", color: COLOR_BLACK, textcolor: "PAX" },
]
const BADGE_RIBBON_DATA = {
  "BYOC": { offset: 0, recolor: false },
  "MEDIA": { offset: 1, recolor: false },
  "OMEGANAUT": { offset: 3, recolor: true }, // but only for non-future?
  "ENFORCER": { offset: 2, recolor: false }
}
const PAX_DATA = {
  "WEST": {
    year: 2004,
    order: 3,
    color: Jimp.rgbaToInt(0, 157, 220, 255)
  },
  "EAST": {
    year: 2010,
    order: 1,
    color: Jimp.rgbaToInt(206, 39, 29, 255)
  },
  "AUS": {
    year: 2013,
    order: 4,
    color: Jimp.rgbaToInt(252, 177, 0, 255)
  },
  "SOUTH": {
    year: 2015,
    order: 0,
    color: Jimp.rgbaToInt(221, 95, 0, 255)
  },
  "DEV": {
    year: 2011,
    order: 2,
    color: Jimp.rgbaToInt(13, 160, 146, 255)
  }
}
const BADGE_IMG_LOOKUP = {
  "AUS": "PAX_AUS.png",
  "DEV": "PAX_DEV.png",
  "EAST": "PAX_EAST.png",
  "SOUTH": "PAX_SOUTH.png",
  "WEST": "PAX_WEST.png"
}
const VERYLARGEWRAP = 1000;
var SORTMAGIC = {
  "2004": {
    "WEST": "2004/08/28"
  },
  "2005": {
    "WEST": "2005/08/26"
  },
  "2006": {
    "WEST": "2006/08/25"
  },
  "2007": {
    "WEST": "2007/08/24"
  },
  "2008": {
    "WEST": "2008/08/29"
  },
  "2009": {
    "WEST": "2009/09/04"
  },
  "2010": {
    "EAST": "2010/03/26",
    "WEST": "2010/09/03"
  },
  "2011": {
    "EAST": "2011/03/11",
    "WEST": "2011/08/26",
    "DEV": "2011/08/24"
  },
  "2012": {
    "EAST": "2012/04/06",
    "WEST": "2012/08/31",
    "DEV": "2012/08/29"
  },
  "2013": {
    "EAST": "2013/03/22",
    "AUS": "2013/07/19",
    "WEST": "2013/08/30",
    "DEV": "2013/08/28"
  },
  "2014": {
    "EAST": "2014/04/11",
    "AUS": "2014/10/31",
    "WEST": "2014/08/29",
    "DEV": "2014/08/27"
  },
  "2015": {
    "SOUTH": "2015/01/29",
    "EAST": "2015/04/22",
    "AUS": "2015/10/30",
    "WEST": "2015/08/28",
    "DEV": "2015/08/26"
  },
  "2016": {
    "SOUTH": "2016/01/29",
    "EAST": "2016/04/22",
    "AUS": "2016/11/04",
    "WEST": "2016/09/02",
    "DEV": "2016/08/31"
  },
  "2017": {
    "SOUTH": "2017/01/29",
    "EAST": "2017/04/01",
    "AUS": "2017/10/31",
    "WEST": "2017/08/31",
    "DEV": "2017/08/30"
  }
};

for( var obj in SORTMAGIC) {
  for( var pax in SORTMAGIC[obj]) {
    SORTMAGIC[obj][pax] = new Date(SORTMAGIC[obj][pax])
  }
}

var comboatlas = Jimp.read("../resources/images/PAX_COMBO.png")

function badgeChanged() {
  var badgeform = $(this).serializeArray()
  var futureidxs = badgeform.filter(function(value) {
    return value.name == "future[]"
  })
  var flatbadgearray = badgeform.filter(function(value) {
    return value.name != "future[]"
  })
  var wraparray = badgeform.filter(function(value) {
    return value.name == "wrap"
  })
  var wrap
  if (wraparray.length == 0) {
    wrap = VERYLARGEWRAP
  } else {
    if (wraparray[0].value == "nowrap") {
      wrap = VERYLARGEWRAP
    } else {
      wrap = parseInt(wraparray[0].value, 10)
      if (isNaN(wrap)) {
        wrap = VERYLARGEWRAP
      }
    }
  }

  var badgearray = []
  const FLATARRAYINC = 4
  for (var i = 0; i < flatbadgearray.length; i += FLATARRAYINC) {
    var flatbadge = flatbadgearray.slice(i, i + FLATARRAYINC)
    var badge = {}
      /* Assume that every 3 items from the form are a group of PAX data */
    if (flatbadge.length == FLATARRAYINC) {
      for (var idx in flatbadge) {
        if (flatbadge[idx].name == "Year[]") {
          badge.year = Number(flatbadge[idx].value)
        }
        if (flatbadge[idx].name == "PAX[]") {
          badge.PAX = flatbadge[idx].value
        }
        if (flatbadge[idx].name == "BadgeBase[]") {
          badge.badgetype = flatbadge[idx].value.slice(0)
        }
        if (flatbadge[idx].name == "Ribbon[]" ) {
          badge.ribbontype = flatbadge[idx].value.slice(0)
        }
      }
      badge.future = false
      badgearray.push(badge)
    }
  }

  /* Tag the proper future entries */
  for (var i = 0; (i < futureidxs.length) && (i < badgearray.length); i++) {
    var badgenum = Math.floor(Number(futureidxs[i].value))
    if (badgenum < badgearray.length) badgearray[badgenum].future = true
  }

  console.log(badgearray)
  makeImage(badgearray, wrap)
}

function getBadge(fw, badge_info) {
  var ybadge = 0;
  var x = (badge_info.future ? 1 : 0) * BADGE_WIDTH;
  var badge = new Jimp(BADGE_WIDTH, BADGE_HEIGHT)

  var basedata = BADGE_BASE_DATA[BADGE_BASE[badge_info.badgetype].value]
  var paxdata = PAX_DATA[badge_info.PAX]

  if (basedata.color == "PAX") {
    var maincolor = paxdata.color
  } else {
    var maincolor = basedata.color
  }
  if (badge_info.future || basedata.textcolor == "PAX") {
    var textcolor = paxdata.color
  } else {
    var textcolor = basedata.textcolor
  }
  var do_ribbon = (badge_info.ribbontype != "NORIBBON")

  if( do_ribbon ) {
    var ribbondata = BADGE_RIBBON_DATA[badge_info.ribbontype]
    var yribbon = BADGE_HEIGHT + ribbondata.offset * RIBBON_HEIGHT
  }
  return comboatlas.then(function(badgeatlas) {
    badge.blit(badgeatlas, 0, 0, x, ybadge, BADGE_WIDTH, BADGE_HEIGHT)
    remapPink(badge, maincolor)

    if( do_ribbon ) {
      var ribbonimage = copycrop(badgeatlas, x, yribbon, RIBBON_WIDTH, RIBBON_HEIGHT)
      if( ribbondata.recolor ) {
        remapPink(ribbonimage, maincolor)
      }
      badge.composite(ribbonimage, 0, 20)
    }
    drawNumber( badge, badgeatlas, badge_info, textcolor )

    return Promise.resolve({ "fw": fw, "badge": badge })
  })
}

function remapPink(image, color) {
  /* This block will replace the magic pink with the right color, while
     saving the alpha */
  var rcolor = Jimp.intToRGBA(color)
  image.scan(0, 0, image.bitmap.width, image.bitmap.height, function(x, y, idx) {
    // x, y is the position of this pixel on the image
    // idx is the position start position of this rgba tuple in the bitmap Buffer
    // this is the image

    if (this.bitmap.data[idx + 0] == 255 && this.bitmap.data[idx + 1] == 0 && this.bitmap.data[idx + 2] == 255) {
      this.bitmap.data[idx + 0] = rcolor.r
      this.bitmap.data[idx + 1] = rcolor.g
      this.bitmap.data[idx + 2] = rcolor.b
      // Leave alpha alone
    }
  })
}

function drawNumber(badge, badgeatlas, info, textcolor) {
  if ((info.year - 2000) <= 0) {
    return;
  }
  var year = Math.floor(info.year)
  var onesDigit = info.year % 10
  var tensDigit = Math.floor(info.year / 10) % 10
  var numWidth = NUM_WIDTHS[onesDigit] + NUM_WIDTHS[tensDigit] + 1;
  var textOffsetX = (BADGE_WIDTH - numWidth) / 2;
  var textOffsetY = info.ribbontype == "NORIBBON" ? TEXT_OFFSET_Y : SPECIAL_TEXT_OFFSET_Y;

  drawDigit(badge, badgeatlas, tensDigit, textcolor, textOffsetX, textOffsetY);
  drawDigit(badge, badgeatlas, onesDigit, textcolor, textOffsetX + NUM_WIDTHS[tensDigit] + 1, textOffsetY);
}

function drawDigit(badge, badgeatlas, digit, color, x, y) {
  var digitOffsetX = NUM_OFFSET
  for (var i = digit - digit % 5; i < digit; i++) {
    digitOffsetX += NUM_WIDTHS[i]
  }
  var digitOffsetY = digit < 5 ? 0 : NUM_HEIGHT

  // copycrop
  digitImg = new Jimp( NUM_WIDTHS[digit], NUM_HEIGHT )
  digitImg.blit( badgeatlas, 0, 0, digitOffsetX, digitOffsetY, NUM_WIDTHS[digit], NUM_HEIGHT );
  remapPink(digitImg, color)
  //digitImg = badgeatlas.clone().crop(digitOffsetX, digitOffsetY, NUM_WIDTHS[digit], NUM_HEIGHT)
  badge.composite(digitImg, x, y) // Do not use blit, it doesn't understand transparency
}

function copycrop( src, x, y, w, h ) {
  ret = new Jimp( w, h )
  ret.blit( src, 0, 0, x, y, w, h )
  return ret
}

function makeImage(badge_array, wrap) {
  const badgeheightoffset = BADGE_HEIGHT + 2
  const badgewidthoffset = BADGE_WIDTH + 2

  var rows = Math.ceil(badge_array.length / wrap)
  rows = (isNaN(rows) || (rows <= 0)) ? 1 : rows
  rows = (rows > 20) ? 20 : rows
  var image = new Jimp(1 + badgewidthoffset * Math.min(badge_array.length, wrap), badgeheightoffset * rows)
  var offset = 1
  if ((badge_array === undefined) || badge_array.length == 0) {
    setBadgeImage(image)
    return // We've setup the image to be blanked, so bail.
  }

  badge_array.sort(function(a, b) {
    adate = new Date(SORTMAGIC[a.year][a.PAX])
    bdate = new Date(SORTMAGIC[b.year][b.PAX])
    return (adate - bdate)
  })

  var promarray = []
  for (var badge_idx = 0; badge_idx < badge_array.length; badge_idx++) {
    // This forwarded data represents the needed order of the badges
    var fw = { "badge_idx": badge_idx }
    promarray.push( getBadge(fw, badge_array[badge_idx]) )
  }

  // When all badges are made, this anonymous callback will resolve
  Promise.all( promarray ).then( function( results ) {
    for( var i in results ) {
      var data = results[i]
      // TODO: Docs say array should be in the same order, so
      // i == data.fw.badge_idx???
      var y = Math.floor(data.fw.badge_idx / wrap)
      var x = Math.floor(data.fw.badge_idx % wrap)
      image.blit(data.badge, x * badgewidthoffset, y * badgeheightoffset)
    }
    // Mind the callback that delays setting the image until the base64 is calculated
    setBadgeImage(image)
  })
}

function setImage(image, id) {
  image.getBase64(Jimp.MIME_PNG, function(err, datauri) {
    document.getElementById(id).src = datauri
  })
}

function setBadgeImage(image) {
  return setImage(image, "badges")
}

$(document).ready(function () {
  //TODO: Add ID to form
  var theform = $("#my-pax").closest("form")
  theform.change( badgeChanged )
  $("#my-pax").change( function() {
    theform.trigger( "change" )
  })
})
