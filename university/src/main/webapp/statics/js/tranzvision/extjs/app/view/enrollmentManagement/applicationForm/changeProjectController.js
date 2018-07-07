Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.changeProjectController',{
    extend: 'Ext.app.ViewController',
    alias: 'controller.changeProjectController',
    //确定
    onChangeProjectEnsure:function(btn) {
        var me = this,
            view = me.getView();
        var form = view.child("form").getForm();

        if(form.isValid()) {
            var fromValue = form.findField("from").getValue();
            var classNameNew =form.findField("classIdNew").rawValue;

            var formParams = form.getValues();
            var formJson = Ext.JSON.encode(formParams);

            var comParams = '"update":['+formJson+']';

            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_TZXM_STD","OperateType":"U","comParams":{'+comParams+'}}';

            Ext.tzSubmit(tzParams,function(responseData){

                if(responseData.nameMsg!=undefined && responseData.nameMsg!="") {
                    Ext.MessageBox.alert("提示","考生【"+responseData.nameMsg+"】在新项目【"+classNameNew+"】中已存在。");
                }

                var panel = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
                var grid ;
                if(fromValue=="YHGL") {
                    //申请用户管理页面进入
                    grid = panel;
                } else {
                    //报名表申请页面进入
                    grid = panel.child("grid");
                }

                grid.store.load();
                view.close();
            },"调整项目成功",true,this);
        }
    },
    //关闭
    onChangeProjectClose:function(btn) {
        this.getView().close();
    }
});