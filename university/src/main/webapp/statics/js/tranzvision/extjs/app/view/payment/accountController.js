﻿Ext.define('KitchenSink.view.payment.accountController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.accountController',
    requires:['KitchenSink.view.payment.accountEditForm'],
    /*查询支付账户*/
    selectAccount:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZFZHGL_COM.TZ_ZFZHGL_STD.TZ_ZFZHGL_VM', //
            condition:
            {
                "TZ_JG_ID":Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
                //alert("查询支付账户结束");
            }
        });
    },
    /*增加支付账户*/
    addAccount:function(btn){
    	//alert("增加支付账户执行");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZFZHGL_COM"]["TZ_ZFZHGL_EDIT_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        //alert("className:"+className);
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZFZHGL_EDIT_STD，请检查配置。');
            return;
        }

		var tzParams = '{"ComID":"TZ_ZFZHGL_COM","PageID":"TZ_ZFZHGL_EDIT_STD","OperateType":"loadPlaformData"}';
		
		var view=this.getView();
		var win = this.lookupReference('accountEditForm');
		// 加载数据
		Ext.tzLoad(tzParams, function(responseData) {
			var fieldList = responseData.root;
			
			if (fieldList == null || fieldList.length == 0) {
				Ext.Msg.alert("提示", "没有支付平台。");
				return;
			} else {
				var store = new Ext.data.Store({
					fields: ['platformId', 'platformName'],
					data:fieldList
				});
				
				if (!win) {
					Ext.syncRequire(className);
					ViewClass = Ext.ClassManager.get(className);
					//新建类
					win = new ViewClass();
					view.add(win);
				}
				var form = win.child("form").getForm();
				form.reset();
				form.findField("accountPlatform").setStore(store);
				win.show();
				}
		});
    },
    saveAccount:function(btn){
    	//alert("执行增加支付账户保存！");
    	var win = btn.findParentByType("window");
        var form=win.down("form").getForm();
        if(!form.isValid() ){
            return false;
        }
        var formParams = form.getValues();
        var tzParams = '{"ComID":"TZ_ZFZHGL_COM","PageID":"TZ_ZFZHGL_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(){
            
           // form.setValues(response.formData);
        	//增加完毕数据 父窗体grid数据重新加载
        	//alert("父窗体："+win.findParentByType("grid"));
			if(win.parentGridStore!=null&&win.parentGridStore!=""){
				//win.findParentByType("grid").getStore().reload();
			}
        },"",true,this);
    },
    //编辑支付账户
    editAccount:function(obj,rowIndex){
    	//alert("执行编辑支付账户！");
    	//拿到当前编辑按钮 直属父类grid
    	var grid=obj.findParentByType("grid");
    	var store=grid.store;
    	var selList=store.getAt(rowIndex);
    	
    	var accountId= selList.get("accountId");
	    //取得进行编辑的窗体
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZFZHGL_COM"]["TZ_ZFZHGL_EDIT_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        //alert("className:"+className);
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZFZHGL_EDIT_STD，请检查配置。');
            return;
        }
    	var view=this.getView();
    	//绑定下拉框
		var tzParams0 = '{"ComID":"TZ_ZFZHGL_COM","PageID":"TZ_ZFZHGL_EDIT_STD","OperateType":"loadPlaformData"}';
		Ext.tzLoad(tzParams0,function(responseData){
			var fieldList = responseData.root;
			if (fieldList == null || fieldList.length == 0) {
				Ext.Msg.alert("提示", "没有支付平台。");
				return;
			} else {
				var store = new Ext.data.Store({
					fields: ['platformId', 'platformName'],
					data:fieldList
				});
				//加载form中的数据
				var tzParams = '{"ComID":"TZ_ZFZHGL_COM","PageID":"TZ_ZFZHGL_STD","OperateType":"QF","comParams":{"accountId":"'+accountId+'"}}';
		        Ext.tzLoad(tzParams,function(response){
		        	//将查询数据结果 放入form中
					Ext.syncRequire(className);
					ViewClass = Ext.ClassManager.get(className);
					var win = new ViewClass();
					view.add(win);
					var form = win.child("form").getForm();
					form.findField("accountPlatform").setStore(store);
					form.findField("accountId").setReadOnly(true);
					form.findField("accountId").setEditable(false);
					form.setValues(response.formData);
					win.show();
		        });
				}
		});

    },
    //新增窗口确定
    ensureAccount:function(btn){
        var win=btn.findParentByType("window");
        var grid=win.findParentByType("grid");
        var store=grid.getStore();
        store.reload();
        win.close();
    },
    /*关闭新增窗口*/
    closeForm:function(btn){
        var win=btn.findParentByType("window");
        win.close();
    },
    //删除账户,从界面中删除，必须保存才能从数据库中删除
    deleteAccount:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore =  btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    //支付账户管理显示界面 关闭
    accountInfoWindowClose:function(btn){
    	var panel=btn.findParentByType("panel");
    	panel.close();
    },
    //支付账户管理显示界面 保存
    accountInfoWindowSave:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //console.log(comParams);
        var tzParams = '{"ComID":"TZ_ZFZHGL_COM","PageID":"TZ_ZFZHGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    }
});

