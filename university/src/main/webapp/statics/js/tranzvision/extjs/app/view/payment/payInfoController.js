Ext.define('KitchenSink.view.payment.payInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.payInfoController',
    requires:['KitchenSink.view.payment.payInfoSerchWin'],
    /*查询支付账户*/
    closePayInfoGrid:function(){
    	//alert("关闭");
    	this.getView().close();
    },
    //主窗体 查询按钮
    queryPayInfo:function(){
    	//alert("查询");
    	//项目名称  项目类型
    	 var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZFCKJGL_COM"]["TZ_ZFCKJGL_SEARCH"];
         if (pageResSet == "" || pageResSet == undefined) {
             Ext.MessageBox.alert('提示', '您没有修改数据的权限');
             return;
         }
         var className = pageResSet["jsClassName"];
         //alert("className:"+className);
         if (className == "" || className == undefined) {
             Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZFCKJGL_SEARCH，请检查配置。');
             return;
         }

 		var view=this.getView();
 		var win = this.lookupReference('payInfoSerchWin');
 		if (!win) {
			//Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			win = new ViewClass();
			view.add(win);
		}
 		win.show();
    },
    //查询窗口“查询” “删除” “关闭”
    queryRecord:function(btn){
    	//alert("执行查询");
    	var win=btn.findParentByType("window");
    	var grid=win.up("grid[name='payInfoGrid']");
    	var store=grid.getStore();
    	alert(grid);
    	var projectName=win.down("textfield[name='projectName']").getValue();
    	var projectType=win.down("combobox[name='projectType']").getValue();
    	
    	if(projectType==null){
    		projectType="";
    	}
    	if(projectName==null){
    		projectName="";
    	}
    	var tzParams = '{"ComID":"TZ_ZFCKJGL_COM","PageID":"TZ_ZFCKJGL_SEARCH","OperateType":"QG","comParams":{"projectName":"'+projectName+'","projectType":"'+projectType+'"}}';
    	store.tzStoreParams=tzParams;
    	store.reload();
    	
    },
    clearRecord:function(btn){
    	//alert("执行清除");
    	var win=btn.findParentByType("window");
    	var projectName=win.down("textfield[name='projectName']");
    	projectName.reset();
    	var projectType=win.down("combobox[name='projectType']");
    	projectType.reset();
    },
    closeWin:function(btn){
    	btn.findParentByType("window").close();
    	//this.getView().close();
    },
    showInfo:function(){
    	alert("show");
    }
});