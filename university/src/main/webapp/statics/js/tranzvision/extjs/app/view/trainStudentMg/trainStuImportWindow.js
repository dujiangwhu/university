Ext.define('KitchenSink.view.trainStudentMg.trainStuImportWindow', {
    extend: 'Ext.window.Window',
    xtype: 'trainStuImportWindow',
    controller: 'trainStudentInfoMg',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainStudentMg.trainStudentController'
    ],
    modal:true,//背景遮罩
    //header:false,
    resizable:true,
    minHeight: 250,
    maxHeight: 600,
	minWidth:500,
    ignoreChangesFlag:true,
    //bodyStyle:'overflow-y:auto;overflow-x:hidden',
	title:"学员导入",
    constructor:function(){
        this.callParent();
    },
    initComponent: function(){
        var me =this;
        Ext.apply(this,{
            items: [{
						xtype:'form',
						name:'uploadExcelForm',
						items:[{
							xtype:'fieldset',
							title: '<span style="font-weight: bold;" class="themeColor">上传Excel文件</span>',
							name:'uploadExcel',
							defaults: {
								anchor: '100%',
								layout: {
									type: 'vbox',
                                    align: 'stretch'
								}
							},
							style:'padding-bottom:10px',
							items:[
								{
									layout:'column',
									items:[
										{
											xtype: 'filefield',
											name: 'orguploadfile',
											itemId:'orguploadfile',
											msgTarget: 'side',
											allowBlank: false,
											anchor: '100%',
											buttonText: '浏览...',
											width:350,
											validator:function(value){
												var excelReg = /\.([xX][lL][sS]){1}$|\.([xX][lL][sS][xX]){1}$/;
												if(!excelReg.test(value)&&value){
													Ext.Msg.alert('提示','文件类型错误,请选择 [xls,xlsx] 格式的Excel文件');
													return '文件类型错误,请选择 [xls,xlsx] 格式的Excel文件';
												}else{
													return true
												}
											}
										},
										{
											xtype:'toolbar',
											name:'importTskExcel',
											padding:0,
											style:'margin-left:20px',
											items:[{
                                                xtype:"button",
                                                width:120,
                                                style:'margin-left:8px',
                                                text:Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_IMPORT.ensureImport", "执行导入"),
                                                handler: 'ensureImport',
                                                labelAlign: 'right',
                                                buttonAlign: 'left',
                                                columnWidth:.2
                                            }]
										}
									]
								}
							]
						}]
					}]
        });
        this.callParent();
    }
});
