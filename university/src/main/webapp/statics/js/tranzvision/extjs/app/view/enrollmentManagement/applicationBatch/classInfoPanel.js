Ext.define('KitchenSink.view.enrollmentManagement.applicationBatch.classInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'classInfoB',
    controller: 'appBatchClass',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.enrollmentManagement.applicationBatch.stuStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn'
    ],
    strSQL:'',
    strConfSearCond:'',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 42;/*button height plus panel body padding*/
            var formHeight = panel.lookupReference('classInfoForm').getHeight();
            var formPadding = 20;
            var grid = panel.child('grid[name=appFormApplicants]');
            grid.setHeight( height- formHeight -buttonHeight-formPadding);
        }
    },
    bodyPadding:10,
    constructor: function (cfg){
        Ext.apply(this,cfg);
        this.callParent();
    },
    initComponent:function(){
        var me = this;
        var appFormStuStore = new KitchenSink.view.enrollmentManagement.applicationBatch.stuStore();

        var submitStateStore = me.initialData.submitStateStore,
            auditStateStore = me.initialData.auditStateStore,
            tzIsMszgStore=me.initialData.tzIsMszgStore,
            orgColorSortStore = me.initialData.orgColorSortStore;

        /*过滤器Options数据*/
        var colorSortFilterOptions=me.initialData.colorSortFilterOptions,
            submitStateFilterOptions=me.initialData.submitStateFilterOptions,
            auditStateFilterOptions=me.initialData.auditStateFilterOptions,
            tzIsMszgFilterOptions=me.initialData.tzIsMszgFilterOptions;

        /*初始颜色类别数据*/
        var initialColorSortData=me.initialData.initialColorSortData;
        var validColorSortStore =  new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_COLOR_SORT_T',
            condition:{TZ_JG_ID:{
                value:Ext.tzOrgID,
                operator:'01',
                type:'01'
            },TZ_COLOR_STATUS:{
                value:'N',
                operator:'02',
                type:'01'
            }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });

        var dynamicColorSortStore = Ext.create("Ext.data.Store",{
            fields:[
                "TZ_COLOR_SORT_ID","TZ_COLOR_NAME","TZ_COLOR_CODE"
            ],
            data:initialColorSortData
        });
        var msjgStore=new KitchenSink.view.common.store.appTransStore("TZ_MSJG");
        var msjgXStore = new KitchenSink.view.common.store.appTransStore("TZ_XMSJG");
        var lqqkStore = new KitchenSink.view.common.store.appTransStore("TZ_LQJG");
        var mszgStore = new KitchenSink.view.common.store.appTransStore("TZ_MSZG");
        var prjID = me.prjID;
        var X=false;
        var noX=false;
        if(prjID=="PRJ_TYPE_31"){
            X=false;
            noX=true;
        }else{
            X=true;
            noX=false;
        }
        
        //是否显示自动标签
        var showAutoTags = false;
        var mbaPrj = Ext.tzGetHardcodeValue("TZ_MBA_PRJ_TYPE");
        if(prjID == mbaPrj){
        	showAutoTags = true;
        }
        
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                bodyPadding:'10px 0 10px 0',
                reference: 'classInfoForm',
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
                    name: 'modalID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.className","班级名称"),
                    name: 'className',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.batchName","申请批次"),
                    name: 'batchName',
                    cls:'lanage_1',
                    readOnly:true
                }
                ]
            },{
                xtype: 'grid',
                height:500,
                header:false,
                name:'appFormApplicants',
                frame: true,
                viewConfig : {
                    enableTextSelection:true
                },
                columnLines: true,
                listeners: {
                    afterrender: {
                        fn: function(stuGrid){
                            stuGrid.getStore().addListener("refresh",
                                function(thisStore){
                                    stuGrid.getView().getSelectionModel().deselectAll();
                                },this);
                        }
                    }
                },
                dockedItems:{
                    overflowHandler: 'Menu',
                    xtype:"toolbar",
                    items:[
                        {
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.query","查询"),
                            iconCls:"query",
                            handler:"queryStudents"
                        },'-',
                        {
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.package","将选中人员材料批量打包"),
                            iconCls:"zip",
                            handler:"packageAndDownload"
                        },'-',
                        {
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.packageTjx","将选中人员的推荐信批量打包"),
                            iconCls:"zip",
                            handler:"packageTjxDownload"
                        },'-',
                        {
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.downloadPackages","查看打包结果并下载"),
                            iconCls:"download",
                            handler:"viewAndDownload"
                        }
                        ,'->',
                        {
                            xtype:'splitbutton',
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.moreOperation","更多操作"),
                            iconCls:  'list',
                            glyph: 61,
                            menu:[  {
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportExcel","导出Excel"),
                                iconCls:"excel",
                                menu:[{
                                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportApplicantsInfo","导出选中人员信息到Excel"),
                                    iconCls:"excel",
                                    name:'exportExcel',
                                    handler:'exportExcelOrDownload'
                                },
                                    {
                                        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.downloadExcel","查看导出结果并下载"),
                                        iconCls:"download",
                                        name:'downloadExcel',
                                        handler:'exportExcelOrDownload'
                                    }]
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.printApplicationForm","打印报名表"),
                                iconCls:"print",
                                handler:'printAppForm'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewEmailSendHis","查看邮件发送历史"),
                                handler:'viewMailHistory'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendEmailSelectedPerson","给选中人发送邮件"),
                                handler:'sendEmlSelPers'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendSmsSelectedPerson","给选中人发送短信"),
                                handler:'sendSmsSelPers'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendEmailSelectedPersonAll","给搜索结果发送邮件"),
                                handler:'sendEmlSelPersOfAll'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendSmsSelectedPersonAll","给搜索结果发送短信"),
                                handler:'sendSmsSelPersOfAll'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cldbAlloqc","搜索结果材料批量打包"),
                                iconCls:"zip",
                                handler:'packageAndDownloadOfAll'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.tjxdbAlloqc","搜索结果推荐信批量打包"),
                                iconCls:"zip",
                                handler:'packageTjxDownloadOfAll'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.dcExcelAlloqc","导出搜索结果信息到Excel"),
                                iconCls:"excel",
                                handler:'exportExcelOfAll'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.changeProject","调整选中人报考项目"),
                                handler:'changeProject'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewMszgMan","面试资格管理"),
                                iconCls:"view",
                                handler:'viewMszgMan'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewMsjgMan","面试结果管理"),
                                iconCls:"view",
                                handler:'viewMsjgMan',
                                hidden:noX
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewXMsjgMan","面试结果管理"),
                                iconCls:"view",
                                handler:'viewXMsjgMan',
                                hidden:X
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewBscjMan","笔试成绩管理"),
                                iconCls:"view",
                                handler:'viewBscjMan'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewLqjgMan","录取结果管理"),
                                iconCls:"view",
                                handler:'viewLqjgMan'
                            }]
                        }
                    ]
                },
                plugins: [
                    {
                        ptype: 'gridfilters',
                        controller: 'appBatchClass'
                    },
                    {
                        ptype: 'cellediting',
                        clicksToEdit: 1
                    },
                    {
                        ptype:'rowexpander',
                        rowBodyTpl : new Ext.XTemplate(
                            '<div class="x-grid-group-title" style="margin-left:80px;">',
                            '<table class="x-grid3-row-table" cellspacing="0" cellpadding="0" border="0" >',
                            '<tpl for="moreInfo">',
                            '<tr style="line-height:30px;">',
                            '<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">{itemName}</td>' +
                            '<td style="font-weight: normal;max-width:800px;">{itemValue}</td>' +
                            '</tr>',
                            '</tpl>',
                            '</table>',
                            '</div>',{}),
                        lazyRender : true,
                        enableCaching : false
                    }
                ],
                selModel: {
                    type: 'checkboxmodel',
                    pruneRemoved: false
                },
                reference: 'stuGrid',
                multiSelect: true,
                store: appFormStuStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 1000,
                    store: appFormStuStore,
                    plugins: new Ext.ux.ProgressBarPager()
                },
                columns: [
                    {
                        xtype:'rownumberer',
                        minWidth:40,
                        maxWidth:80
                    },
                    {
                        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.basicInfo","基本信息"),
                        lockable   : false,
                        menuDisabled: true,
                        columns:[
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.name","姓名"),
                                dataIndex: 'stuName',
                                width:100,
                                lockable   : false,
                                filter: {
                                    type: 'string'
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewApplicationID","面试申请号"),
                                dataIndex: 'interviewApplicationID',
                                width:110,
                                lockable   : false,
                                filter: {
                                    type: 'string'
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.submitState","提交状态"),
                                dataIndex: 'submitState',
                                lockable   : false,
                                width: 95,
                                filter: {
                                    type: 'list',
                                    options: submitStateFilterOptions
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = submitStateStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return submitStateStore.getAt(index).get("TSDesc");
                                        }

                                        return "";
                                    }
                                }
                            },{
                                xtype:'datecolumn',
                                format:'Y-m-d',
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.submitDate","提交时间"),
                                dataIndex: 'submitDate',
                                lockable   : false,
                                width: 105,
                                filter: {
                                    type: 'date',
                                    format:'Y-m-d',
                                    active:true
                                }
                            },/*{
                             text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.auditState","评审状态"),
                             dataIndex: 'auditState',
                             lockable   : false,
                             width: 95,
                             filter: {
                             type: 'list',
                             options: auditStateFilterOptions
                             },
                             renderer:function(v){
                             if(v){
                             var index = auditStateStore.find('TValue',v,0,false,true,true);
                             if(index>-1){
                             return auditStateStore.getAt(index).get("TSDesc");
                             }

                             return "";
                             }
                             }
                             },*/{
                            	text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.autoTags","自动标签"),
                                dataIndex: 'autoTags',
                                lockable   : false,
                                width: 140,
                                menuDisabled: true,
                    			sortable: false,
                    			hidden: !showAutoTags,
                    			xtype: 'templatecolumn',
                    			tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
                    				labels: function(values){
                    					var labels = "";
                    					var val = values.autoTags;
                    					if(val.trim() != ""){
                    						var labelArr = val.split("|");
                    						for(var i=0;i<labelArr.length;i++){
                    							labels = labels 
                    							+ '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">' 
                    							+ labelArr[i] 
                    							+ '</span>';
                    						}
                    					}
                    					return labels;
                    				}
                    			})
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.clpsksFlag","是否材料评审"),
                                dataIndex: 'clpsksFlag',
                                lockable   : false,
                                width: 110,
                                filter: {
                                    type: 'list',
                                    options: [
                                        ["Y", "是"],
                                        ["N", "否"]
                                    ]
                                },
                                renderer:function(v){
                                    if(v=="Y"){
                                        return "是";
                                    } else {
                                        return "否";
                                    }
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.colorType","类别"),
                                dataIndex: 'colorType',
                                lockable   : false,
                                width: 140,
                                filter: {
                                    type: 'list',
                                    options: colorSortFilterOptions
                                },
                                editor: {
                                    xtype: 'combo',
                                    queryMode:'local',
                                    valueField: 'TZ_COLOR_SORT_ID',
                                    displayField: 'TZ_COLOR_NAME',
                                    triggerAction: 'all',
                                    editable : false,
                                    triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    },
                                    store:dynamicColorSortStore,
                                    tpl: Ext.create('Ext.XTemplate',
                                        '<tpl for=".">',
                                        '<div class="x-boundlist-item"><div class="x-colorpicker-field-swatch-inner" style="margin-top:6px;width:30px;height:50%;background-color: #{TZ_COLOR_CODE}"></div><div style="margin-left:40px;display: block;  overflow:  hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;"> {TZ_COLOR_NAME}</div></div>',
                                        '</tpl>'
                                    ),
                                    displayTpl: Ext.create('Ext.XTemplate',
                                        '<tpl for=".">',
                                        '{TZ_COLOR_NAME}',
                                        '</tpl>'
                                    ),
                                    listeners: {
                                        focus: function (combo,event, eOpts) {
                                            var selList = this.findParentByType("grid").getView().getSelectionModel().getSelection();

                                            var colorSortID =selList[0].raw.colorType;

                                            var arrayData = new Array();
                                            for(var i=0;i<validColorSortStore.getCount();i++){
                                                arrayData.push(validColorSortStore.data['items'][i].data);
                                            }
                                            if(colorSortID.length>0&&validColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID)==-1){
                                                var tmpRec = orgColorSortStore.getAt(orgColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID));
                                                arrayData.push(tmpRec.data);
                                            }
                                            if(arrayData.length<1){
                                                arrayData.push({TZ_COLOR_SORT_ID:'',TZ_COLOR_NAME:'',TZ_COLOR_CODE:''});
                                            }
                                            combo.store.loadData(arrayData);
                                        },
                                        blur: function (combo,event, eOpts) {
                                            combo.store.loadData(initialColorSortData);
                                        }
                                    }
                                },
                                renderer:function(v){
                                    if(v){
                                        var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,false,true,true);
                                        if(rec>-1){
                                            return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
                                        }else{
                                            return Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.pleaseSelect","请选择...");
                                        }
                                    }
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewBatch","面试批次"),
                                dataIndex: 'interviewBatch2',
                                //sortable: false,
                                width:140,
//                    			editor: {
//                    				xtype: 'combobox',
//                    	            store: new KitchenSink.view.common.store.appTransStore("TZ_BMBSH_IB"),
//                    	            valueField: 'TValue',
//                    	            displayField: 'TSDesc'
//                    				}
                                editor: {
                                    xtype: 'combobox',
                                    store:{
                                        fields: [{name:'value'},{name:'desc'}],
                                        data: [{value: '第一批', desc: '第一批'},{value: '第二批', desc: '第二批'},{value: '第三批', desc: '第三批'},{value: '第四批', desc: '第四批'},{value: '第五批', desc: '第五批'},{value: '第六批', desc: '第六批'}]
                                    },
                                    displayField: 'desc',
                                    valueField: 'value',
                                    triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    },
                                },
                                renderer: function(v){
                                    if (v !== null || v !== undefined || v !== '') {
//                    				if(v.length>0){
                                        if(v=='第一批'){
                                            return "第一批";
                                        }else if(v=='第二批'){
                                            return "第二批";
                                        }else if(v=='第三批'){
                                            return "第三批";
                                        }else if(v=='第四批'){
                                            return "第四批";
                                        }else if(v=='第五批'){
                                            return "第五批";
                                        }else if(v=='第六批'){
                                            return "第六批";
                                        }
//                        				else{
//                        					return "其他批次";
//                        				}
                                    }else{
                                        return " ";
                                    }

                                }
                            },
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.isMszg","是否有面试资格"),
                                dataIndex: 'isMszg',
                                lockable   : false,
                                width: 110,
                                filter: {
                                    type: 'list',
                                    options: tzIsMszgFilterOptions
                                },
                                editor: {
                                    xtype: 'combobox',
                                    store:mszgStore,
                                    valueField: 'TValue',
                                    displayField: 'TSDesc',
                                    triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    }
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = mszgStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return mszgStore.getAt(index).get("TSDesc");
                                        }

                                        return "";
                                    }
                                }
                            }, {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewDate","面试日期"),
                                dataIndex: 'interviewDate',
                                lockable   : false,
                                width: 110,
                                filter: {
                                    type: 'date',
                                    format:'Y-m-d',
                                    active:true
                                },
                                renderer:Ext.util.Format.dateRenderer('Y-m-d '),
                                editor:{
                                    xtype:'datefield',
                                    format:'Y-m-d',
                                    anchor: '100%'
                                }
                            },
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.checkinTime","报到时间"),
                                dataIndex: 'checkinTime',
                                lockable   : false,
                                width: 110,
                                filter: {
                                    type: 'string'
                                },
                                editor:{
                                    xtype:'timefield',
                                    format:'H:i',
                                    anchor: '100%'
                                },
                                renderer:Ext.util.Format.dateRenderer('H:i')
                            },
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewResult","面试结果"),
                                dataIndex: 'interviewResult',
                                lockable   : false,
                                hidden:noX,
                                width: 110,
                                filter: {
                                    type: 'string'
                                },
                                editor: {
                                    xtype: 'combobox',
                                    store:msjgStore,
                                    valueField: 'TValue',
                                    displayField: 'TSDesc',
                                    triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    }
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = msjgStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return msjgStore.getAt(index).get("TSDesc");
                                        }

                                        return "";
                                    }
                                }
                            },
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewResultX","面试结果"),
                                dataIndex: 'interviewResultX',
                                lockable   : false,
                                width: 110,
                                hidden:X,
                                filter: {
                                    type: 'string'
                                },
                                editor: {
                                    xtype: 'combobox',
                                    store:msjgXStore,
                                    valueField: 'TValue',
                                    displayField: 'TSDesc',
                                    triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    }
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = msjgXStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return msjgXStore.getAt(index).get("TSDesc");
                                        }

                                        return "";
                                    }
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.enrollStatus","录取状况"),
                                dataIndex: 'enrollStatus',
                                lockable   : false,
                                width: 110,
                                filter: {
                                    type: 'string'
                                },
                                editor: {
                                    xtype: 'combobox',
                                    store:lqqkStore,
                                    valueField: 'TValue',
                                    displayField: 'TSDesc',
                                    triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    }
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = lqqkStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return lqqkStore.getAt(index).get("TSDesc");
                                        }

                                        return "";
                                    }
                                }
                            }]
                    }
                    ,{
                        xtype:'actioncolumn',
                        align:'center',
                        menuDisabled: true,
                        width:80,
                        items:[
                            {
                                tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.viewApplicationForm","查看报名表"),
                                sortable:false,
                                handler: "viewApplicationForm",
                                iconCls:'preview'
                            },
                            {
                                tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.printApplicationForm","打印报名表"),
                                sortable:false,
                                handler: "printAppForm",
                                iconCls:'print'
                            },
                            {
                                tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.auditApplicationForm","报名表审核"),
                                sortable:false,
                                handler: "auditApplicationForm",
                                iconCls:'audit'
                            }
                        ]
                    },{
                        xtype:'appFormDynamicColumn',
                        classID:me.classID,
                        store:appFormStuStore
                    }
                ]
            }]
        })
        this.callParent();
    },
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applicantsList","报考学生列表"),
    bodyStyle:'overflow-y:hidden;overflow-x:hidden',
    buttons: [{
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.save","保存"),
        iconCls:"save",
        handler: 'onStuInfoSave'
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onStuInfoEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
        iconCls:"close",
        handler: 'onStuInfoClose'
    }]
});
