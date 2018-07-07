Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.uploadCommitLetter', {
    extend: 'Ext.window.Window',
	reference: 'uploadCommitLetter',
    xtype: 'uploadCommitLetter',
	width: 500,
	height: 170,
	minWidth: 300,
	minHeight: 150,
    columnLines: true,
    ignoreChangesFlag:true,
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.commit","上传承诺书"),
	layout: 'fit',
	resizable: true,
	modal: true,
	closeAction: 'hide',

    items: [{
			xtype: 'form',
			reference: 'uploadRefLetterForm',
			layout: {
				type: 'vbox',       // Arrange child items vertically
				align: 'stretch'    // 控件横向拉伸至容器大小
			},
			border: false,
			bodyPadding: 10,
			bodyStyle:'overflow-y:auto;overflow-x:hidden',
			
			fieldDefaults: {
				msgTarget: 'side',
				labelWidth: 100,
				labelSeparator:'',
				labelStyle: 'font-weight:bold'
			},
			items: [{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'oprid',
					hidden:true
			},{
				xtype: 'textfield',
				fieldLabel: '',
				name: 'mshId',
				hidden:true
			},{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'currentRowIndex',
					hidden:true
			},{
				layout: {
					type: 'column'
				},
				bodyStyle:'padding:10px 0 10px 0',
				xtype: 'form',  
				items:[{
					width:450,
					xtype: 'filefield',
					fieldLabel: '',
					buttonText: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.browse","浏览"),
					name: 'orguploadfile',
					listeners:{
						change:function(bt, value, eOpts){
							addAttach(bt, value, eOpts);
						}
					}
				}]
			}
			]
		}],
		buttons: [{
			text:  Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.ensure","确定"),
			iconCls:"ensure",
			handler: 'onUploadComLetterEnsure'
		}, {
			text:  Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.close","关闭"),
			iconCls:"close",
			handler: 'onUploadComLetterClose'
		}]
});

function addAttach(bt, value, eOpts){
	
	var form = bt.findParentByType("form").getForm();

	
}
