$(function() {
    $('.lsm-scroll').slimscroll({
        height: 'auto',
        position: 'right',
        railOpacity: 1,
        size: "5px",
        opacity: .4,
        color: '#fffafa',
        wheelStep: 5,
        touchScrollStep: 50
    });
    $('.lsm-container ul ul').css("display", "none");
    $('.lsm-sidebar a').on('click',
    function() {
        $('.lsm-scroll').slimscroll({
            height: 'auto',
            position: 'right',
            size: "8px",
            color: '#9ea5ab',
            wheelStep: 5,
            touchScrollStep: 50
        });
        if (!$('.left-side-menu').hasClass('lsm-mini')) {
            $(this).parent("li").siblings("li.lsm-sidebar-item").children('ul').slideUp(200);
            if ($(this).next().css('display') == "none") {
                $(this).next('ul').slideDown(200);
                $(this).parent('li').addClass('lsm-sidebar-show').siblings('li').removeClass('lsm-sidebar-show');
            } else {
                $(this).next('ul').slideUp(200);
                $(this).parent('li').removeClass('lsm-sidebar-show');
            }
        }
    });
    $('.lsm-mini-btn svg').on('click',
    function() {
        if ($('.lsm-mini-btn input[type="checkbox"]').prop("checked")) {
            $('.lsm-sidebar-item.lsm-sidebar-show').removeClass('lsm-sidebar-show');
            $('.lsm-container ul').removeAttr('style');
            $('.left-side-menu').addClass('lsm-mini');
            $('.left-side-menu').stop().animate({
                width: 60
            },
            200);
        } else {
            $('.left-side-menu').removeClass('lsm-mini');
            $('.lsm-container ul ul').css("display", "none");
            $('.left-side-menu').stop().animate({
                width: 240
            },
            200);
        }
    });
    $(document).on('mouseover', '.lsm-mini .lsm-container ul:first>li',
    function() {
        $(".lsm-popup.third").hide();
        $(".lsm-popup.second").length == 0 && ($(".lsm-container").append("<div class='second lsm-popup lsm-sidebar'><div></div></div>"));
        $(".lsm-popup.second>div").html($(this).html());
        $(".lsm-popup.second").show();
        $(".lsm-popup.third").hide();
        var top = $(this).offset().top;
        var d = $(window).height() - $(".lsm-popup.second>div").height();
        if (d - top <= 0) {
            top = d >= 0 ? d - 8 : 0;
        }
        $(".lsm-popup.second").stop().animate({
            "top": top
        },
        100);
    });
    $(document).on('mouseover', '.second.lsm-popup.lsm-sidebar > div > ul > li',
    function() {
        if (!$(this).hasClass("lsm-sidebar-item")) {
            $(".lsm-popup.third").hide();
            return;
        }
        $(".lsm-popup.third").length == 0 && ($(".lsm-container").append("<div class='third lsm-popup lsm-sidebar'><div></div></div>"));
        $(".lsm-popup.third>div").html($(this).html());
        $(".lsm-popup.third").show();
        var top = $(this).offset().top;
        var d = $(window).height() - $(".lsm-popup.third").height();
        if (d - top <= 0) {
            top = d >= 0 ? d - 8 : 0;
        }
        $(".lsm-popup.third").stop().animate({
            "top": top
        },
        100);
    });
    $(document).on('mouseleave', '.lsm-mini .lsm-container ul:first, .lsm-mini .slimScrollBar,.second.lsm-popup ,.third.lsm-popup',
    function() {
        $(".lsm-popup.second").hide();
        $(".lsm-popup.third").hide();
    });
    $(document).on('mouseover', '.lsm-mini .slimScrollBar,.second.lsm-popup',
    function() {
        $(".lsm-popup.second").show();
    });
    $(document).on('mouseover', '.third.lsm-popup',
    function() {
        $(".lsm-popup.second").show();
        $(".lsm-popup.third").show();
    });
});