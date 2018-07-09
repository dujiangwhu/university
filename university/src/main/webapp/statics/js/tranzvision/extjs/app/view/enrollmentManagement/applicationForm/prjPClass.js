Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.prjPClass', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationForm.classController',
        'KitchenSink.view.enrollmentManagement.applicationForm.prjClassStore'
    ],
    xtype: 'prjPClass',
    controller: 'appFormClass',
    columnLines: true,
    style:"margin:8px",
    multiSelect: true,
    title: '项目类别',
    
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"onPrjClose"}]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:"queryPrj"}
        ]
    }],
    initComponent: function () {
      var store = new KitchenSink.view.enrollmentManagement.applicationForm.prjClassStore();

        Ext.apply(this, {
            columns: [{
                text:  Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_PRJ_STD.prjID","项目分类编号"),
                dataIndex: 'prjID',
                width: 120
            },{
                text:  Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_PRJ_STD.prjName","分类名称"),
                sortable: true,
                dataIndex: 'prjName',
                minWidth: 75,
                flex:1
           },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_PRJ_STD.bmbsh","报名表审核"),
                dataIndex: 'viewUser',
                width:140,
                renderer:function(){
                    return '<a href="javascript:void(0)" >报名表审核</a>';
                },
                listeners:{
                    click:'viewSiteUser'
                }
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 20,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});