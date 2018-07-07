Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrBatchInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'ksDataDrBatchInfo',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.ksShujuDr.ksShujuDrBatchInfoStore',
		'KitchenSink.view.ksShujuDr.ksShujuDrBatchInfoController'
    ],
	controller: 'ksShujuDrBatchInfoController',
    bodyPadding:10,
    constructor: function (obj){
		this.gridColumn=obj;
        this.callParent();
    },
    listeners: {
        resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
        var buttonHeight = 42; //button height plus panel body padding
        var formHeight = 30;
        var formPadding = 20;
        var grid = panel.child('grid[name=ksList]');
        grid.setHeight(height - formHeight - buttonHeight - formPadding);
        }
        },
    initComponent:function(){
        var me = this;
        var ksShujuDrBatchInfoStore = new KitchenSink.view.ksShujuDr.ksShujuDrBatchInfoStore();
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                bodyPadding:'10px 0 10px 0',
                reference: 'drBatchInfoForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    name: 'batchId',
                    hidden:true
                },{
                    xtype: 'textfield',
                    name: 'tplId',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.tplId","导入描述信息"),
                    name: 'tplName',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.filename","文件名"),
                    name: 'filename',
                    cls:'lanage_1',
                    readOnly:true
                }
                ]
            },{
                xtype: 'grid',
               // height:500,
                header:false,
                name:'ksList',
                frame: true,
                viewConfig : {
                    //enableTextSelection:true
                },
                columnLines: true,
                plugins: [
                    {
                        ptype: 'gridfilters',
                        //controller: 'appFormClass'
                    },
                    {
                        ptype: 'cellediting',
                        clicksToEdit: 1
                    }
                ],
                reference: 'ksInfoGrid',
                multiSelect: true,
                store: ksShujuDrBatchInfoStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 1000,
                    store: ksShujuDrBatchInfoStore,
                    plugins: new Ext.ux.ProgressBarPager()
                },
                columns: this.gridColumn
            }]
        })
        this.callParent();
    },
    title: Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.drKsList","导入批次信息"),
    bodyStyle:'overflow-y:hidden;overflow-x:hidden',
    buttons: [{
        text: Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.close","关闭"),
        iconCls:"close",
        handler: 'onInfoClose'
    }]
});
