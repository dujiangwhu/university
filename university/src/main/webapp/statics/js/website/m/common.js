
	
	$(document).ready(function(){
    tab(".tab_login .login_tit",".tab_login .login_tabnote","add_f","mousedown");
    tab(".zhaos .list",".zhaos .tabNote","list_on","mousedown");
     tab(".Tab_school .tit_tab",".Tab_school .tabN_school","school_on","mousedown");
      $(".tit_tab  li").click(function(){
                $(this).parents("").find("li").removeClass("school_on");
                $(this).parents("").find(".school_t").hide();
                $(this).addClass("school_on");
                $(this).find(".school_t").show();
              });
      /*弹窗洲选择*/
   $(".chose_list li").click(function(){
    $(this).parents("ul").find("li").removeClass("chose_on");
    $(this).addClass("chose_on");
  });

    /*弹窗国家选择*/
   $(".country_list li").click(function(){
    $(this).parents("ul").find("li").removeClass("chose_on");
    $(this).addClass("chose_on");
  });
    //返回顶部
        var offset = 300,
        offset_opacity = 1200,
        scroll_top_duration = 700,
        $back_to_top = $('.cd-top');

    $(window).scroll(function(){
        ( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if( $(this).scrollTop() > offset_opacity ) { 
            $back_to_top.addClass('cd-fade-out');
        }
    });

    $back_to_top.on('click', function(event){
        event.preventDefault();
        $('body,html').animate({
            scrollTop: 0 ,
            }, scroll_top_duration
        );
    });
    // 切换
     tab(".zhaos .list",".zhaos .tabNote","list_on","mousedown");
     tab(".Tab_school .tit_tab",".Tab_school .tabN_school","school_on","mousedown");
     tab(".tab_login .login_tit",".tab_login .login_tabnote","add_f","mousedown");
     
      $(".tit_tab  li").click(function(){
                $(this).parents("").find("li").removeClass("school_on");
                $(this).parents("").find(".school_t").hide();
                $(this).addClass("school_on");
                $(this).find(".school_t").show();
              });
    //关闭弹窗
     $("#pop_btn").click(function(){
       $(".shade1").hide();
       $(".pop").hide();
      
    });

      $(".share_remind").click(function(){
       $(".shade1").show();
       $(".pop").show();
      
    });

     //查看流程信息
   $(".search_step").click(function(){
         
         $(this).children('i').toggleClass('step_down');
         $(this).parents().next(".record_body").toggle();

     });
   //展开文字
   $(".slide").click(function(){

         $(this).children('i').toggleClass('slide_up');
         $(this).prev().toggleClass('slide_wz');
     });
    //修改默认radio样式
  $("input[type='radio'].inp-radio").each(function(){
    var _inpRadio = $(this),
      _bRadio = _inpRadio.next(),
      name = _inpRadio.attr("name") || '';
    if(_inpRadio.is(":checked")){
      _bRadio.addClass("bon-radio");
    }else{
      if(_bRadio.hasClass("bon-radio"))_bRadio.removeClass("bon-radio");
    }
    _bRadio.click(function(){
      _inpRadio.trigger("click"); 
      if($("input[type='radio']:checked")){
        $("input[type='radio']:checked").next().addClass("bon-radio");  
        $("input[type='radio']:not(:checked)").next().removeClass("bon-radio");
      }
    });
  });
   $("input[type='checkbox'].inp-chb1").each(function(){
    var _inpChb = $(this),
      _bChb = _inpChb.next();
    if(_inpChb.is(":checked")){
      _bChb.addClass("bon1-chb");
    }else{
      if(_bChb.hasClass("bon1-chb"))_bChb.removeClass("bon1-chb");
    }
    _bChb.click(function(){
      _inpChb.trigger("click");
      if(_inpChb.is(":checked")){
        _bChb.addClass("bon1-chb");
      }else{
        _bChb.removeClass("bon1-chb");
      }
    })
  })
});
 $(window).load(function(){
 
 
});

$(window).resize(function(){
 
  
});


function tab(nav,content,on,type)
      {
        $(nav).children().bind(type,(function(){
          var $tab=$(this);
          var tab_index=$tab.prevAll().length;
          var $content = $(content).children();
          $(nav).children().removeClass(on);
          $tab.addClass(on);
          $content.hide();
          $content.eq(tab_index).show();
        }));
      }



