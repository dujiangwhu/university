Ext.define('KitchenSink.view.enrollProject.applicationUserMg.applicationPrj', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.applicationUserMg.applicationPrjController',
        'KitchenSink.view.enrollProject.applicationUserMg.applicationPrjStore'
    ],
    xtype: 'applicationPrj',
    controller: 'appPrjController',
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
      var store = new KitchenSink.view.enrollProject.applicationUserMg.applicationPrjStore();

        Ext.apply(this, {
            columns: [{
                text:  Ext.tzGetResourse("TZ_BMGL_APPL_COM.TZ_BMGL_PRJ_STD.prjID","项目分类编号"),
                dataIndex: 'prjID',
                width: 120
            },{
                text:  Ext.tzGetResourse("TZ_BMGL_APPL_COM.TZ_BMGL_PRJ_STD.prjName","分类名称"),
                sortable: true,
                dataIndex: 'prjName',
                minWidth: 75,
                flex:1
           },{
                text:Ext.tzGetResourse("TZ_BMGL_APPL_COM.TZ_BMGL_PRJ_STD.appList","查看申请用户"),
                dataIndex: 'viewUser',
                width:140,
                renderer:function(){
                    return '<a href="javascript:void(0)" >查看申请用户</a>';
                },
                listeners:{
                    click:'viewAppUser'
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
