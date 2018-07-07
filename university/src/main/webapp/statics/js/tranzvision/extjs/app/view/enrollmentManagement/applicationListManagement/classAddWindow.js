Ext.define('KitchenSink.view.enrollmentManagement.applicationListManagement.classAddWindow', {
    extend: 'Ext.window.Window',
    xtype: 'classAddWindow',
    controller: 'appBatchClass',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationListManagement.classController'
    ],
    title:  Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.addClassTitle","添加评审批次"),
    reference:'classAddWindow',
    ignoreChangesFlag:true,
    width:500,
    y:10,
    actType: 'add',
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        items: [
        	{
        		layout: {
                    type: 'column'
                },
                items:[
                	{
                        xtype: 'textfield',
                        fieldLabel: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.className","班级名称"),
                        name: 'className',
                        width:'100%',
                        editable: false,
                        allowBlank:false,
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler: "pmtSearchClass"
                            }
                        }
                    },
                    {
                    	xtype:'textfield',
                    	name:'classID',
                    	hidden: true
                    }                    
                ]
        	},
        	{
        		layout: {
                    type: 'column'
                },
                style:'margin-top:10px;',
                items:[
                	{
                        xtype: 'textfield',
                        fieldLabel: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.batchName","批次名称"),
                        name: 'batchName',
                        width:'100%',
                        editable: false,
                        allowBlank:false,
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler: "pmtSearchBatch"
                            }
                        }
                    },
                    {
                    	xtype:'textfield',
                    	name:'batchID',
                    	hidden: true
                    }
                ]
        	}            
        ]
    }],
    buttons: [
    {
        text:Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.ensure","确定"),
        iconCls:"ensure",
        //handler: 'ensureAttr'
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();

            win.doSave(btn,win,true);
        }
    }, 
    {
        text: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.close","关闭"),
        iconCls:"close",
        handler: function(btn){
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.close();
        }
    }],
    doSave:function(btn,win,close){
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	
        //保存
        var form = win.child("form").getForm();
        if(!form.isValid()){
            return false;
        }
        var formParams = form.getValues();

        var tzParams = '{"ComID":"TZ_PS_MGR_COM","PageID":"TZ_PS_MGR_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzLoadAsync(tzParams,function(response){
        	var result = response.result;
            if(result=="success"){
            	store.load();
            	win.close();
            }else{
            	Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.prompt","提示"),result);
            }
        	
        },"",false,this);
    }
});
