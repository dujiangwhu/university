Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.importMsIsBan', {
    extend: 'Ext.window.Window',
    xtype: 'importMsInfoWin',
    controller: 'msRelativeController',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],
	
    modal:true,//背景遮罩
    resizable:false,
    width: 600,
    height: 165,
    ignoreChangesFlag:true,
    title: '批量导入学生面试信息',
            
    constructor:function(conf){
        this.classID = conf.classID;
        this.GridReload = conf.callback;      
        this.callParent();
    },
    initComponent: function(){
        var me =this;

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding:10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                	xtype: 'label',
                	text: '上传Excel文件'
                },{
                	layout:'column',
                    items:[{
                        xtype: 'filefield',
                        name: 'orguploadfile',
                        itemId:'orguploadfile',
                        msgTarget: 'side',
                        allowBlank: false,
                        anchor: '100%',
                        buttonText: '浏览...',
                        columnWidth: 1,
                        validator:function(value){
                            var excelReg = /\.([xX][lL][sS]){1}$|\.([xX][lL][sS][xX]){1}$/;
                            if(!excelReg.test(value)&&value){
                                Ext.Msg.alert('提示','文件类型错误,请选择 [xls,xlsx] 格式的Excel文件');
                                return '文件类型错误,请选择 [xls,xlsx] 格式的Excel文件';
                            }else{
                                return true
                            }
                        }
                    }]
                }]
            }]
        });
        this.callParent();
    },
    buttons: [{
		text: '确定导入',
		iconCls:"ensure",
		handler: 'onEnsureImportMsInfo'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType('window').close();
		}
	}]
});
