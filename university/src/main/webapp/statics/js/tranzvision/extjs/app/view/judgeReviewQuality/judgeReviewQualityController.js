Ext.define('KitchenSink.view.judgeReviewQuality.judgeReviewQualityController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.judgeReviewQualityController',
    //查询
    queryJudge:function(btn) {
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.TZ_CLPW_INFO_VW',
            condition:{
                TZ_JG_ID:Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //查看
    viewJudgeDetail:function(grid,rowIndex) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CLPW_PSZL_COM"]["TZ_PSZL_DTL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PSZL_DTL_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        clsProto = ViewClass.prototype;

        if (clsProto.themes) {
            clsProto.themeInfo = clsProto.themes[themeName];

            if (themeName === 'gray') {
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }


        var record = grid.store.getAt(rowIndex);
        var judgeOprid = record.data.judgeOprid;
        var judgeDlzhId = record.data.judgeDlzhId;
        var interviewPass = record.data.interviewPass;
        if(interviewPass!=0) {
            interviewPass = interviewPass+"%";
        }

        cmp = new ViewClass();

        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            form.findField("judgeOprid").setValue(judgeOprid);
            form.findField("judgeDlzhId").setValue(judgeDlzhId);
            form.findField("interviewPass").setValue(interviewPass);

            var grid = panel.down('grid');
            var tzStoreParams = '{"judgeOprid":"'+judgeOprid+'"}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
        });

        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }

    },
    //关闭
    closeJudge:function(btn) {
        this.getView().close();
    }
});