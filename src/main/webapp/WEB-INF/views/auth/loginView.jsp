<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .login-container {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 40px;
            width: 300px;
        }
        .login-container h2 {
            margin-bottom: 20px;
            text-align: center;
        }
        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .login-container input[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 10px 20px;
            cursor: pointer;
            width: 100%;
            transition: background-color 0.3s;
        }
        .login-container input[type="submit"]:hover {
            background-color: #0056b3;
        }
</style>
<script type="text/javascript">

</script>
</head>
<body>
<div>
<c:choose>
    <c:when test="${empty principal}">  <!-- "$ 띄어쓰기 하면 절대안됨 -->
        <ul class="navbar-nav">
            <li class="nav-item"><a class="nav-link" href="/loginForm">로그인</a></li>
        </ul>
    </c:when>
    <c:otherwise>
     <ul class="navbar-nav">
            <li class="nav-item"><a class="nav-link" href="/loginForm">로그아웃</a></li>
        </ul>
    </c:otherwise>
 </c:choose>
 </div>
 <div class="login-container">
        <h2>타임리프로 로그인</h2>
        <form id="login-form" action="#" method="POST">
            <input type="text" name="userName" placeholder="아이디" id=username required>
            <input type="password" name="password" placeholder="비밀번호" id="password" required>
            <input type="button" value="로그인" id="loginButton">
            <input type="button" value="회원가입">
        </form>
 </div>

<script>
$(document).ready(function() {
    $('#loginButton').click(function() {
        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            type: 'POST',
            url: 'login',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {
                console.log('로그인 성공! 토큰:', response);
                getUserInfo(response.accessToken); // accessToken으로 변경

            },
            error: function(xhr, status, error) {
                console.error('로그인 실패:', error);
                // 여기에 로그인 실패 시의 처리를 추가할 수 있습니다.
            }
        });
    });
});

function getUserInfo(accessToken) { // refreshToken이 아닌 accessToken으로 변경
    $.ajax({
        type: 'POST',
        url: '/auth/member/info',
        headers: {
            'Authorization': 'Bearer ' + accessToken // accessToken 사용
        },
        success: function(memberInfo) {
            console.log('사용자 정보:', memberInfo);
        },
        error: function(xhr, status, error) {
            console.error('사용자 정보 가져오기 실패:', error);
        }
    });
}

    </script>
</body>
</html>