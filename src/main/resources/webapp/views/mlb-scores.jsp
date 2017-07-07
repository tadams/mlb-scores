<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>MLB Game Scores</title>
<style type="text/css">
table {
    border-collapse: collapse;
}
table, th, td {
    border: 2px solid black;
    font-size: 20px;
    padding: 10px;
    text-align: center;
}
input, .form-input {
    font-size: 27px;
    font-weight: bold;
    display: inline;
}
input[type=submit] {
    padding:3px 15px;
    background:#ccc;
    border:0 none;
    cursor:pointer;
    -webkit-border-radius: 5px;
    border-radius: 5px;
}
.error {
    color: red;
}
img {
  vertical-align: middle;
}
</style>
</head>
<body>
	<br>
	<center>
    <img src="images/mlb.png"/>
    <form class="form-input" name="gameDate" method="get" action="scores">
            Game Date: <input type="text" name="gameDate" placeholder="yyyy-MM-dd">
            <input type="submit">
    </form>
    <c:if test="${!errors.isEmpty()}">
        <c:forEach var="error" items="${errors}">
            <h4 class="error">${error}</h4>
        </c:forEach>
    </c:if>
	<c:if test="${gamesForDate.isNotEmpty()}">
	<p/>
	<hr/>
        <h2>Games Played: <fmt:formatDate pattern="MMMM dd, yyyy" value="${gamesForDate.gamesDate}"/></h2>
        <table>
            <tr>
                <th>Away</td><th>Team</th><th>Runs</th>
                <th>Home</td><th>Team</th><th>Runs</th>
            </tr>
                <c:forEach var="game" items="${gamesForDate.games}" varStatus="loop">
                    <tr>
                        <td><img src="images/${game.awayTeamNameCode}.jpg"</td>
                        <td>${game.awayTeamName}</td>
                        <td>${game.awayTeamRuns}</td>
                        <td><img src="images/${game.homeTeamNameCode}.jpg"</td>
                        <td>${game.homeTeamName}</td>
                        <td>${game.homeTeamRuns}</td>
                    </tr>
                </c:forEach>
        </table>
    </c:if>
	</center>
</body>
</html>
