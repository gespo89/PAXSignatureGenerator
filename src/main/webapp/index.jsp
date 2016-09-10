<%--
  Created by IntelliJ IDEA.
  User: Geoff
  Date: 8/28/2016
  Time: 6:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <title>PAX Signature Generator</title>
  </head>
  <body>
  <script language="Javascript" type="text/javascript">
    var counter = 1;
    function addInput(divName){
      var newdiv = document.createElement('div');
      newdiv.innerHTML = "<h4>PAX " + (counter + 1) + "</h4>Year:<input type=\"text\" name=\"Year[]\"><br />PAX:<select name=\"PAX[]\"><c:forEach items="${applicationScope['paxen']}" var="pax"><option value=\"${pax}\">${pax}</option></c:forEach></select></select><br />Badge Type:<select name=\"Badge[]\"><c:forEach items="${applicationScope['badgetypes']}" var="badgetype"><option value=\"${badgetype}\">${badgetype}</option></c:forEach></select><br />Future:<input type=\"checkbox\" name=\"future[]\" value=\"" + counter + "\"><br />"
      document.getElementById(divName).appendChild(newdiv);
      counter++;
    }
  </script>
  <H1>PAX Signature Generator</H1>
  <form method="POST" action="signature.png">
    <div id="dynamicInput">
      <h4>PAX 1</h4>
      Year:<input type="text" name="Year[]"><br />
      PAX:<select name="PAX[]">
        <c:forEach items="${applicationScope['paxen']}" var="pax">
          <option value="${pax}">${pax}</option>
        </c:forEach>
      </select>
      </select><br />
      Badge Type:
      <select name="Badge[]">
          <c:forEach items="${applicationScope['badgetypes']}" var="badgetype">
              <option value="${badgetype}">${badgetype}</option>
          </c:forEach>
      </select><br />
      Future:
      <input type="checkbox" name="future[]" value="0"><br />
    </div>
    <input type="button" value="Add another PAX" onClick="addInput('dynamicInput');">
    <input type="submit" value="Generate!"/>
  </form>
  </body>
</html>
