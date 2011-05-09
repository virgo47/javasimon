<%@page import="org.javasimon.*"%>
<%@page import="org.javasimon.utils.*"%>
<html>
<head>
</head>
<body>
Java Simon monitoring

<pre>
<%=SimonUtils.simonTreeString(SimonManager.getRootSimon())%>
</pre>
</body>
</html>