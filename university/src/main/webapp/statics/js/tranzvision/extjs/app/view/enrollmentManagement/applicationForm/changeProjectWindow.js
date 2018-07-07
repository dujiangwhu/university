Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.changeProjectWindow',{
    extend:'Ext.window.Window',
    xtype:'changeProjectWindow',
    controller:'changeProjectController',
    requires:[
        'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.enrollmentManagement.applicationForm.changeProjectController'
    ],
    title:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_TZXM_STD.changProjectTitle","调整项目"),
    width:500,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor:function(obj) {
        this.classId = obj.classId;
        this.opridList = obj.opridList;
        this.nameList = obj.nameList;
        this.initialData = obj.initialData;
        this.from = obj.from;
        this.callParent();
    },
    initComponent:function() {
        var classStore = Ext.create('Ext.data.Store',{
            fields:['TZ_CLASS_ID','TZ_CLASS_NAME'],
            data:this.initialData.classData
        });

        var batchStore = Ext.create('Ext.data.Store',{
            fields:['TZ_BATCH_ID','TZ_BATCH_NAME'],
            data:[]
        });

        var classBatchData = this.initialData.classBatchData;

        Ext.apply(this,{
            items:[{
                xtype:'form',
                layout:{
                    type:'vbox',
                    align:'stretch'
                },
                border:false,
                bodyPadding:10,
                items:[{
                    xtype:'textfield',
                    name:'classId',
                    value:this.classId,
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'opridList',
                    value:this.opridList,
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'nameList',
                    value:this.nameList,
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'from',
                    value:this.from,
                    hidden:true
                },{
                    xtype:'combo',
                    fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_TZXM_STD.classIdNew","新项目"),
                    name:'classIdNew',
                    store:classStore,
                    autoShow:true,
                    valueField:'TZ_CLASS_ID',
                    displayField:'TZ_CLASS_NAME',
                    queryMode:'local',
                    editable:false,
                    allowBlank:false,
                    listeners:{
                        select: function (combo,record,eOpts) {
                            var form = this.findParentByType("form").getForm();
                            var batchIdNewField = form.findField("batchIdNew");
                            batchIdNewField.clearValue();

                            var selectValue = this.getValue();

                            for(var i=0;i<classBatchData.length;i++) {
                                if(classBatchData[i].TZ_CLASS_ID==selectValue) {
                                    batchIdNewField.store.loadData(classBatchData[i].TZ_BATCH_LIST);
                                }
                            }

                        }
                    }
                    
                },{
                    xtype:'combo',
                    fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_TZXM_STD.batchIdNew","申请批次"),
                    name:'batchIdNew',
                    store:batchStore,
                    autoShow:true,
                    valueField:'TZ_BATCH_ID',
                    displayField:'TZ_BATCH_NAME',
                    queryMode:'local',
                    editable:false,
                    //allowBlank:false
                }]
            }]
        });
        this.callParent();
    },
    buttons:[{
        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_TZXM_STD.ensure","确定"),
        iconCls:'ensure',
        handler:'onChangeProjectEnsure'
    },{
        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_TZXM_STD.close","关闭"),
        iconCls:'close',
        handler:'onChangeProjectClose'
    }]
});