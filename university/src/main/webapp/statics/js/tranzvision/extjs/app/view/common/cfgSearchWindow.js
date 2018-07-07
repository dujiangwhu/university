Ext.define('KitchenSink.view.common.cfgSearchWindow', {
    extend: 'Ext.window.Window',
    xtype: 'cfgSearchWindow',
    title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00082"),/*查询*/
    width: 500,
    minWidth: 650,
    minHeight: 250,
    resizable: true,
    modal: true,
    closeAction: 'destroy',
    ignoreChangesFlag: true,//让框架程序不要提示用户保存的属性设置
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    requires:[
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.common.store.promptStore',
        'KitchenSink.view.common.store.comboxStore'
    ],
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },

    initComponent: function(){
        var me = this;
       
        var userID=TranzvisionMeikecityAdvanced.Boot.loginUserId;
        var jgID=Ext.tzOrgID;
        console.log(userID);
        console.log(jgID);
        
				Ext.apply(Ext.form.field.VTypes, {  
				  promptVildate:  function(value,field) {  
					  var flag = false;  
					  var fldName = field.getName().replace("-value","");
					  Ext.Ajax.request({
					     async: false,
					     url: Ext.tzGetGeneralURL,
					     params: {
					     	 "tzParams":'{"ComID":"TZ_COMMON_CFG_COM","PageID":"TZ_COMMON_CFG_STD","OperateType":"validatePrompt","comParams":{"cfgSrhId":"'+ me.cfgSrhId +'","fldName":"'+ fldName +'","fldValue":"'+ value +'"}}'
					     },
					     async:false,  
							 success: function(response){
									var responseText = eval( "(" + response.responseText + ")" );
									
									if(responseText.comContent.success == "true"){
										flag = true;
									}
								}
						});
					  return flag;   
				  },  
				  promptVildateText: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00100")/*输入的值不存在*/
				});
				
				var tzParams = '{"ComID":"TZ_COMMON_CFG_COM","PageID":"TZ_COMMON_CFG_STD","OperateType":"QF","comParams":{"cfgSrhId":"'+this.cfgSrhId+'","currentUser":"'+userID+'","currentrOganization":"'+jgID+'"}}';
//				var tzParams = '{"ComID":"TZ_COMMON_CFG_COM","PageID":"TZ_COMMON_CFG_STD","OperateType":"QF","comParams":{"cfgSrhId":"'+this.cfgSrhId+'"}}';

        //搜索条件
        var conItems = [];
        //加载数据
        Ext.tzLoadAsync(tzParams,function(responseData){
            var formData = responseData.formData;
            for(var fieldName in formData) {
                //字段类型
                var fldType = formData[fieldName].fldType;
                //如果是下拉框,则初始化下拉框默认值;
                var downCondition = null;
                var downConditionStr = "";
                if(fldType == "06"){
                    downConditionStr = '{ "TZ_JG_ID":{ "value" : "EMBA", "operator":"01", "type":"01"}}';
                    downCondition = Ext.JSON.decode(downConditionStr);
                }
                //promptTable;
                var promptTable = formData[fieldName].promptTable;
                var promptTableFld = formData[fieldName].promptTableFld;
                var promptTableFldDesc = formData[fieldName].promptTableFldDesc;
                var translateFld = formData[fieldName].translateFld;
                var transCondition = {};
                if(translateFld != ""){
                    var transConditionStr = '{ "TZ_ZHZJH_ID":{ "value" : "' + translateFld + '", "operator":"01", "type":"01"}}';
                    transCondition = Ext.JSON.decode(transConditionStr);
                }
                //操作符是否只读;
                var operatorReadOnly = formData[fieldName].operatorReadOnly;
                //搜索字段是否字段;
                var fldReadOnly = formData[fieldName].fldReadOnly;
                if(fldReadOnly == true){
                    //如果字段只读，则操作符一定只读;
                    operatorReadOnly = fldReadOnly;
                }
                //是否隐藏;
                var fldHidden = formData[fieldName].fldHidden;
                //搜索条件
                var condition = me.condition;

                var fieldValue = "";
				
                for(var defaultFieldName in condition) {
                    if(fieldName ==defaultFieldName ){
                        fieldValue = condition[defaultFieldName];
                        break;
                    }
                }
                var typeField = {};
                switch (fldType)
                {
                    case '01'://字符串
                        typeField = {
                            xtype: 'textfield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            name: fieldName+'-value',
                            value: fieldValue,
                            hidden: fldHidden,
                            readOnly: fldReadOnly,
                            listeners: {
                                specialkey: function (textfield, e) {
                                    if (e.getKey() == Ext.EventObject.ENTER) {
                                        me.searchCfg();
                                    }
                                }
                            }
                        };
                        break;
                    case '02'://数字
                        typeField = {
                            xtype: 'numberfield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            value: fieldValue,
                            readOnly: fldReadOnly,
                            hidden: fldHidden,
                            name: fieldName+'-value',
                            listeners: {
                                specialkey: function (textfield, e) {
                                    if (e.getKey() == Ext.EventObject.ENTER) {
                                        me.searchCfg();
                                    }
                                }
                            }
                        };
                        break;
                    case '03'://日期
                        typeField = {
                            xtype: 'datefield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            format: 'Y-m-d',
                            value: fieldValue,
                            readOnly: fldReadOnly,
                            hidden: fldHidden,
                            name: fieldName+'-value',
                            listeners: {
                                specialkey: function (textfield, e) {
                                    if (e.getKey() == Ext.EventObject.ENTER) {
                                        me.searchCfg();
                                    }
                                }
                            }
                        };
                        break;
                    case '04'://时间
                        typeField = {
                            xtype: 'timefield',
                            columnWidth: 1,
                            increment: 30,
                            format: 'H:i',
                            hideEmptyLabel: true,
                            hidden: fldHidden,
                            //value: fieldValue,
                            readOnly: fldReadOnly,
                            name: fieldName+'-value',
                            listeners: {
                                specialkey: function (textfield, e) {
                                    if (e.getKey() == Ext.EventObject.ENTER) {
                                        me.searchCfg();
                                    }
                                }
                            }
                        };
                        break;
                    case '05'://prompt
                        typeField = {
                            layout: {
                                type: 'column'
                            },
                            items:[
                                {
                                    //columnWidth: 1,
                                    minWidth:200,
                                    xtype: 'textfield',
                                    hideEmptyLabel: true,
                                    name: fieldName+'-value',
                                    readOnly: fldReadOnly,
                                    hidden: fldHidden,
                                    value: fieldValue,
                                    validateOnChange: false,
                                    validateOnBlur: true,
                                    vtype: 'promptVildate',
                                    triggers: {
                                        search: {
                                            cls: 'x-form-search-trigger',
                                            handler: function(fld){
                                                var thisName = fld.getName();
                                                var fldName = thisName.replace("-value","");
                                                var promptTable =formData[fldName].promptTable;
                                                var promptTableFld =formData[fldName].promptTableFld;
                                                var promptTableFldDesc =formData[fldName].promptTableFldDesc;
                                                var promptTableFldLabel = formData[fldName].promptTableFldLabel;
                                                var promptTableDescFldLabel = formData[fldName].promptTableDescFldLabel;
                                                var promptTableDefaultFld = formData[fldName].promptTableDefaultFld;
                                                me.pmtSearchScheduModel(fld,promptTable,promptTableFld,promptTableFldDesc,promptTableFldLabel,promptTableDescFldLabel,promptTableDefaultFld);
                                            }
                                        }
                                    },
                                    listeners: {
                                        specialkey: function (textfield, e) {
                                            if (e.getKey() == Ext.EventObject.ENTER) {
                                                me.searchCfg();
                                            }
                                        }
                                    }
                                }
                                ,{
                                    xtype: 'displayfield',
                                    hideLabel: true,
                                    maxWidth:180,
                                    style:'margin-left:10px',
                                    name: fieldName+'-desc'
                                }
                            ]
                        };
                        break;
                    case '06'://下拉框
                        typeField = {
                            xtype: 'combobox',
                            hideEmptyLabel: true,
                            width: 357,
                            //autoSelect: false,
                            store: new KitchenSink.view.common.store.comboxStore({
                                recname: promptTable,
                                condition: transCondition,
                                result: promptTableFld+","+promptTableFldDesc
                            }),
                            valueField: promptTableFld,
                            displayField: promptTableFldDesc,
                           //typeAhead: true,
                            queryMode: 'remote',
                            name: fieldName+'-value',
                            value: fieldValue,
                            readOnly: fldReadOnly,
                            forceSelection: true,
                            multiSelect: true,
                            hidden: fldHidden,
                            listeners:{
                                beforequery: function (query) {
                                    var downConditionStr = "";
                                    var combo = query.combo;
                                    var store = combo.store;
                                    var comboName = combo.getName( );
                                    var form = combo.findParentByType("form").getForm();
                                    combo.setValue("");

                                    try{
                                        var TZ_ZHZJH_ID = store.condition.TZ_ZHZJH_ID;
										if(TZ_ZHZJH_ID == undefined){
											var fldName = comboName.replace("-value", "");
											var downTableDefaultFld = formData[fldName].promptTableDefaultFld;
											
											Ext.Array.forEach( downTableDefaultFld, function(defFld){
												
												var fldVal = form.findField(defFld["TZ_FILTER_GL_FLD"]+"-value").getValue();
												if(downConditionStr ==""){
													downConditionStr ='"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'", "operator":"01","type": "01"	}';
												}else{
													downConditionStr =downConditionStr+',"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'", "operator":"01","type": "01"	}';
												}
											});

											if(downConditionStr ==""){
												downConditionStr="{}";
											}else{
												delete combo.lastQuery;
												downConditionStr = "{"+downConditionStr+"}";
											}

											var tzStoreParams = '{"OperateType":"COMBOX","recname":"'+store.recname+'","condition":'+downConditionStr+',"result":"'+store.result+'"}';
											store.tzStoreParams = tzStoreParams;
										}
									}catch(e){
									}
                                },
                                specialkey: function (textfield, e) {
                                    if (e.getKey() == Ext.EventObject.ENTER) {
                                        me.searchCfg();
                                    }
                                },
                                change: function(combo,newValue, oldValue){
                                	  var comboName = combo.getName( );
                                    var form = combo.findParentByType("form").getForm();
                                    var opreteFieldName = comboName.replace("-value", "-operator");
                                    var opreteFieldValue = form.findField(opreteFieldName).getValue();
                                    
                                    if(opreteFieldValue == '10'||opreteFieldValue == '13'){
                                	  	//combo.multiSelect = true;
                                	  }else{
                                	  	//combo.multiSelect = false;
                                	  	combo.collapse();
                                	  }
                                	  
                                		
                                }
                            }
                        };
                        break;
                    default:
                        typeField = {
                            xtype: 'textfield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            value: fieldValue,
                            readOnly: fldReadOnly,
                            hidden: fldHidden,
                            name: fieldName+'-value'
                        };
                };
                var fieldLabel = Ext.tzGetResourse(me.cfgSrhId+"-"+formData[fieldName].fldDesc.replace(/"/g,'\"'),formData[fieldName].fldDesc);
                var operator = formData[fieldName].operator;
                Ext.Array.each(operator, function(item,index,allItems) {
                    switch(item.transId){
                        case '01':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00088");
                            break;
                        case '02':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00089");
                            break;
                        case '03':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00090");
                            break;
                        case '04':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00091");
                            break;
                        case '05':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00092");
                            break;
                        case '06':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00093");
                            break;
                        case '07':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00094");
                            break;
                        case '08':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00095");
                            break;
                        case '09':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00096");
                            break;
                        case '10':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00097");
                            break;
                        case '11':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00098");
                            break;
                        case '12':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00099");
                            break;
                        case '13':item.transDesc = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00102");
                            break;
                    }
                });
                var fieldItem = {
                    layout:'column',
                    bodyPadding: 5,
                    items:[{
                        xtype: 'combobox',
                        labelWidth: 120,
                        width: 250,
                        labelSeparator:'',
                        readOnly: operatorReadOnly,
                        hidden: fldHidden,
                        hideLabel: fldHidden,
                        editable:false,
                        fieldLabel:fieldLabel,
                        store:{
                            fields: [{name:'transId'},{name:'transDesc'}],
                            data: operator
                        },
                        displayField: 'transDesc',
                        valueField: 'transId',
                        value: formData[fieldName].operator[0].transId,
                        name: fieldName+'-operator',
                        listeners:{
                        	change: function(meThisComb, newValue, oldValue){
                        		  
                        		  //var comboName = meThisComb.getName();
                        		  //var form = meThisComb.findParentByType("form").getForm();
                        		  
                              //var FieldName = comboName.replace("-operator", "-value");
                              //var Field = form.findField(FieldName);
                              //var fieldXtype = Field.getXType();
                              //if(fieldXtype=="combobox" || fieldXtype=="combo"){
                              	//Field.multiSelect = false;
                              	//Field.getPicker( ).selModel.mode = 'SINGLE';
                              	//Field.getPicker( ).refresh();
                              	//console.log(Field.getInitialConfig("multiSelect"));
                              	//Field.render();
                              	//Field.update("",false);
                              	//Field.render();
                              	//Field.render();
                              	//meThisComb.findParentByType("form").remove(Field);
                              	//meThisComb.findParentByType("form").add(Field);
                              	//Field.reset();   
																//Field.hide();
																//Field.removeCls() ; 
																//Field.render(); 
																
                              	
                              //}
                        	}
                        }
                    },typeField]
                }
                conItems.push(fieldItem);
            };
			/*
			var dataSetItem = {
				xtype: 'fieldset',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				name:"dataSet",
				title: "数据集查询",
                bodyPadding: 5,
				items:[{
					xtype: 'checkboxfield',
					boxLabel: '<span style="font-weight:bold;">所有听众</span>',
					margin: '0 0 5 120',
					hideLabel: true,
					inputvalue:'true',
					name: 'fldHide'
				},{
					xtype: 'checkboxfield',
					boxLabel: '<span style="font-weight:bold;">学院听众</span>',
					margin: '0 0 5 120',
					hideLabel: true,
					inputvalue:'true',
					name: 'fldHide'
				}]
			};
			conItems.push(dataSetItem);
			*/
			var dataSetItems = [];
			var formDataSet = responseData.formDataSet;
			var isDisplay = responseData.isDisplay;
			console.log(isDisplay);
			if(formDataSet.length>0){
				console.log(formDataSet);
				for(var dataSet_i=0;dataSet_i<formDataSet.length;dataSet_i++){
					var defaultChecked = false;
					if(formDataSet[dataSet_i]["TZ_FLTDST_DEFAULT"] == "on"){
						defaultChecked = true;
					}
					var dataSetItem = {
						xtype: 'checkboxfield',
						boxLabel: '<span style="font-weight:bold;">' + formDataSet[dataSet_i]["TZ_FLTDST_DESC"]+ '</span>',
						margin: '0 0 5 120',
						hideLabel: true,
						inputvalue:'Y',
						checked: defaultChecked,
						name: formDataSet[dataSet_i]["TZ_FLTDST_ORDER"] + "-dataset"
					}
					dataSetItems.push(dataSetItem);
				};
				
				var dataSet= {
					xtype: 'fieldset',
					layout: {
						type: 'vbox',
						align: 'stretch'
					},
					name:"dataSet",
					title: '<span style="color:#66B3FF;">数据集查询</span>',
					bodyPadding: 5,
					items:dataSetItems
				};
				
				if (isDisplay=="Y") {
					conItems.push(dataSet);
				}
				
				
			}
        });

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                ignoreLabelWidth: true,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelStyle: 'font-weight:bold'
                },
                dockedItems:[{
                    xtype:"toolbar",
                    items:["->",
                        {
                            xtype:'splitbutton',
                            text:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00083")/*更多操作*/,
                            iconCls:  'list',
                            glyph: 61,
                            menu:[{
                                text:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00084")/*基本搜索*/,
                                listeners:{
                                    click: function (bt, e, eOpts) {
                                        me.changeSearchModel(bt,true );
                                    }
                                }
                            },{
                                text:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00085")/*高级搜索*/,
                                listeners:{
                                    click: function (bt, e, eOpts) {
                                        me.changeSearchModel(bt,false);
                                    }
                                }
                            }]
                        }
                    ]
                }],
                items: conItems,
                buttonAlign: 'left'
            }]
            ,
            buttons: [
                {
                    text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00086")/*搜索*/,
                    iconCls:"search",
                    handler: function(btn){
                        me.searchCfg();
                    }
                },{
                    text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00087")/*清除*/,
                    iconCls:"clean",
                    handler: function(btn){
                        //搜索信息表单
                        var form = btn.findParentByType("window").child("form").getForm();
                        //重置表单
                        form.reset();
                    }
                },
                {
                    text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00047")/*关闭*/,
                    iconCls:"close",
                    handler: function(btn){
                        //获取窗口
                        var win = btn.findParentByType("window");
                        //修改密码信息表单
                        var form = win.child("form").getForm();
                        //关闭窗口
                        win.close();
                    }
                }]

        });
        this.callParent();
    },
    searchCfg: function(){

        //搜索信息表单
        var form = this.child("form").getForm();
        if (form.isValid()) {
            //表单数据
            var formParams = form.getValues();

            var cfgSrhId = this.cfgSrhId;
			console.log(formParams);

            var tzSearchParams = '{"cfgSrhId":"'+cfgSrhId+'","condition":'+Ext.encode(formParams)+'}';

            //回调函数
            this.callback(tzSearchParams);
            //关闭窗口
            this.close();
        }
    },
    changeSearchModel: function(bt,model){

        var form = bt.findParentByType("form");
        var baseForm = form.getForm();
        baseForm.reset();
        var comboboxArr = form.query( "combobox" );


        Ext.Array.forEach( comboboxArr, function(cmbox){
            var oprName = cmbox.getName();
            var isOpreate =  oprName.indexOf("-operator");
            if(isOpreate > 0 ){
                var fldName = oprName.replace("-operator", "-value");
                var fld = baseForm.findField(fldName);
                var fldReadOnly = fld.getEl().dom.getElementsByTagName("input")[0].getAttribute("readonly");
                if(fldReadOnly=='readonly' ||fldReadOnly=='true'){

                }else{
                    cmbox.setReadOnly(model);
                }
            }
        });
    },
    pmtSearchScheduModel: function(btn,promptTable,promptTableFld,promptTableFldDesc,promptTableFldLabel, promptTableDescFldLabel,promptTableDefaultFld){
        var srhConFieldsStr = '{"'+promptTableFld+'":{ "desc":"'+promptTableFldLabel+'","operator":"07","type":"01"},"'+promptTableFldDesc+'":{"desc":"'+promptTableDescFldLabel+'","operator":"07","type":"01"}}';
        var srhConFields = Ext.JSON.decode(srhConFieldsStr);
        var presetFieldsStr = "";
        var form = btn.findParentByType("form").getForm();

        Ext.Array.forEach( promptTableDefaultFld, function(defFld){

            var fldVal = form.findField(defFld["TZ_FILTER_GL_FLD"]+"-value").getValue();
            if(presetFieldsStr ==""){
                presetFieldsStr ='{"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'","type": "01"	}}';
            }else{
                presetFieldsStr =presetFieldsStr+","+'{"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'","type": "01"	}}';
            }
        });

        if(presetFieldsStr ==""){
            presetFieldsStr="{}";
        }

        var presetFields = Ext.JSON.decode(presetFieldsStr);
        var srhresultStr = '{"'+promptTableFld+'": "'+promptTableFldLabel+'","'+promptTableFldDesc+'": "'+promptTableDescFldLabel+'"	}';
        var srhresult = Ext.JSON.decode(srhresultStr);

        Ext.tzShowPromptSearch({
            recname: promptTable,
            searchDesc: '查询',
            maxRow:20,
            condition:{
                presetFields:presetFields,
                srhConFields:srhConFields
            },
            srhresult: srhresult,
            multiselect: false,
            callback: function(selection){
                form.findField(promptTableFld+"-value").setValue(selection[0].data[promptTableFld]);
                form.findField(promptTableFld+"-desc").setValue(selection[0].data[promptTableFldDesc]);
            }
        });
    },
    constructor: function (config) {
        //可配置搜索查询;
        this.cfgSrhId = config.cfgSrhId;
        //搜索条件
        this.condition = config.condition;
        //回调函数
        this.callback = config.callback;
        this.callParent();
    }
});
