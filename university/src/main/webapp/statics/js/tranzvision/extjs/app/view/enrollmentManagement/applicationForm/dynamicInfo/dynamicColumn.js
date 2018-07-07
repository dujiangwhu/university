Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn', {
    extend: 'Ext.grid.Column',
    xtype:'appFormDynamicColumn',
    header:'<div style="text-align:left">'+Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applicationInfo","报名表信息")+'</div>',
    menuDisabled: true,
    classID:'',
    store:{},
    initComponent:function(){
    	/*不采用动态列，报名表信息读取固定表字段
        var me = this;
        var columnArray;
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzLoadGridColumns","comParams":{"classID":"'+this.classID+'"}}';
        Ext.tzLoadAsync(tzParams,function(responseData){
            columnArray = responseData;
        });

        Ext.apply(this,{
            columns:columnArray
        });

        var dynamicFields = ['classID','oprID','appInsID','stuName','submitState','submitDate','auditState', 'colorType','moreInfo'];
        for(var i=0;i<columnArray.length;i++){
            var name = columnArray[i].dataIndex;
            var type = columnArray[i].filter.type=='date'?'date':'string';
            switch(columnArray[i].filter.type)
            {
                case "number":
                    type="number";
                    break;
                case "date":
                    type="date";
                    break;
                default:
                    type= "string"
            };
            dynamicFields.push({name:name,type:type});
        }
        var store = this.store;
        var model = new Ext.data.Model({
            fields:dynamicFields
        });
        store.setModel(model);
        this.callParent();
        */
    	var me = this;
        var columns =[{
            text: "手机号码",
            dataIndex: 'mobile',
            lockable   : false,
            width: 110,
            filter: {
                type: 'string'
            }
        },{
            text: "电子邮件",
            dataIndex: 'email',
            lockable   : false,
            width: 110,
            filter: {
                type: 'string'
            }
        },{
            text: "公司（单位）名称",
            dataIndex: 'companyName',
            lockable   : false,
            width: 130,
            filter: {
                type: 'string'
            }
        },{
            text: "公司（单位）行业",
            dataIndex: 'companyIndustry',
            lockable   : false,
            width: 130,
            filter: {
                type: 'string'
            }
        },{
            text: "公司（单位）性质",
            dataIndex: 'companyNature',
            lockable   : false,
            width: 130,
            filter: {
                type: 'string'
            }
        },{
            text: "院校",
            dataIndex: 'school',
            lockable   : false,
            width: 120,
            filter: {
                type: 'string'
            }
        }];
        
        Ext.apply(me,{
            columns:columns
        });
        
        me.callParent();
    }
});
