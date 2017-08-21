<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>MLB Scores Health Check</title>
</head>
<body style="font-family:sans-serif; font-size:16px;">
    <h1>Health Check</h1>
    <div>
        <h3>Environment Config: ${env}</h3>
        <ul>
            <li>Application Version: ${version}
            <li>CPU Count: ${cpuCnt}
            <li>CPU Load %: ${cpuLoad}
            <li>Memory (in use / max): ${memUse}
        </ul>
    </div>
    <hr />
</body>
</html>
