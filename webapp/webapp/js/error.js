var minH  = 520;  // 屏幕的最小高度
$(function(){
	adaptScreen();
	$(window).resize(function(){
		adaptScreen();
	});

	$("#aback").click(function(e){ 
		e.preventDefault();
		history.back(-1);
	});
});

function adaptScreen(){
	var sh = $(window).height();
	sh  = sh > minH ? sh : minH;
	var mh = sh - 120, lh = (mh - 200) / 2;
	$(".main").css("height", mh + "px");
}