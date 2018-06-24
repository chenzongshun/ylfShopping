var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("YLF_USER_COOKIE");// 需要和浏览器的cookie名字一样，cookie名是服务端发回给浏览器的UUID
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8090/user/token/" + _ticket,
			dataType : "jsonp",		// jquery封装好了跨域请求的，需要设置数据类型为jsonp
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					// var html = username + "，欢迎来到宜立方购物网！<a href=\"http://www.e3mall.cn/user/logout.html\" class=\"link-logout\">[退出]</a>";
					var html = username + "，欢迎来到宜立方商城！<a href=\"http://localhost:8090/page/login\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});