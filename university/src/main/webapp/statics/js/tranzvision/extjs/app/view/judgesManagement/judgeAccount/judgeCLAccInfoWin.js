Ext.define('KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccInfoWin', {
    extend: 'Ext.panel.Panel',
    requires: [
          'KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccController'
    ],
    xtype: 'judgeCLAccInfo',
	reference: 'judgeCLAccInfoWindow',
    controller: 'judgeCLAccController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    resizable: true,
    modal: true,
    closeAction:'destroy',
	title: '创建评委账号',
	actType: 'add',
	judType: [],
	pGrid: {},
	
	listeners:{
		close:function(panel, eOpts){
			//panel.pGrid.getEl().unmask();
		},
//		afterrender:function(){
//			var judTpItems = [];
//			var tzParams = '{"ComID":"TZ_JUDGE_CLACC_COM","PageID":"TZ_JUDCLINFO_STD","OperateType":"QO","comParams":{}}';
//            Ext.tzLoad(tzParams,
//                function(responseData) {
//                    console.log(responseData);
//                    console.log(responseData.total);
//                    for(var k=0;k<responseData.total;k++){
//    		    		//alert(jugTypeStore.getAt(k).get("TZ_JUGTYP_ID"));
//    		    		var tItems = {
//    							xtype:'checkboxfield',
////    							boxLabel: judTypeArr[i].TZ_JUGTYP_NAME,  
//    							boxLabel: responseData.root[k].TZ_JUGTYP_NAME,  
//    							name: 'judgeType',
////    							inputValue: judTypeArr[i].TZ_JUGTYP_ID,
//    							inputValue: responseData.root[k].TZ_JUGTYP_ID,  
//    							style: {
//    								marginRight: '20px'
//    							}
//    						};	
//    		    		judTpItems.push(tItems);
//    		    	}
//                });
//		}
	},
	
	constructor: function(jugTypeStore){
		this.judType = jugTypeStore;
		this.callParent();
	},
	
	initComponent:function(){
		var tzParams = '{"ComID":"TZ_JUDGE_CLACC_COM","PageID":"TZ_JUDCLINFO_STD","OperateType":"tzGetJudgeType","comParams":{}}';
		var judTpItems = [];
		Ext.tzLoadAsync(tzParams,function(responseData){
			var arr=responseData.total;
			for(var k=0;k<arr;k++){
				var tItems = {
//					xtype:'checkboxfield',
					boxLabel: responseData.root[k].TZ_JUGTYP_NAME,
					name: 'judgeType',
					inputValue: responseData.root[k].TZ_JUGTYP_ID,
					style: {
						marginRight: '20px'
					}
	        	};
	        	judTpItems.push(tItems);
			}
			
		});
		/*for(var i=0; i<judTypeArr.length; i++){
			for(var i=0; i<2; i++){
				var tItems = {
					xtype:'checkboxfield',
					boxLabel: judTypeArr[i].judgeTypeName,  
					name: 'judgeType',
					inputValue: judTypeArr[i].judgeTypeId
				};	
				judTpItems.push(tItems);
			}
		}*/
		/*for(var i=1; i<judTypeArr.length; i++){
			if (i == 1){
				var tItems = {
					// fieldLabel:'评委类型',
					xtype:'checkboxfield',
					store:jugTypeStore,
					queryMode:'lcoal',
					boxLabel: judTypeArr[i].B,  
//					boxLabel: 'TZ_JUGTYP_ID',  
					name: 'judgeType',
					inputValue: judTypeArr[i].B,
//					inputValue: 'TZ_JUGTYP_NAME',
					style: {
						marginRight: '20px'
					}
				};	
			}else {
				var tItems = {
					xtype:'checkboxfield',
					boxLabel: judTypeArr[i].judgeTypeName,  
					name: 'judgeType',
					inputValue: judTypeArr[i].judgeTypeId,
					style: {
						marginRight: '20px'
					}
				};	
			}
			judTpItems.push(tItems);
		}
		
		for(var i=0; i<judTypeArr.length; i++){
			for(var i=0; i<2; i++){
				var tItems = {
					xtype:'checkboxfield',
					boxLabel: judTypeArr[i].judgeTypeName,  
					name: 'judgeType',
					inputValue: judTypeArr[i].judgeTypeId
				};	
				judTpItems.push(tItems);
			}
		}*/
		
		Ext.apply(this,{
			 items: [{
				xtype: 'form',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				autoScroll : true,
				bodyStyle : 'overflow-x:visible; overflow-y:scroll',
				bodyPadding: 10,
           //      bodyStyle:'overflow-y:auto;overflow-x:hidden',
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 100,
					labelStyle: 'font-weight:bold'
				},
				
				items: [{
					xtype: 'textfield',
//					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.accountNo","用户账号"),
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_CLACC_COM.TZ_JUDCLINFO_STD.accountNo","用户账号"),
					name: 'accountNo',
					allowBlank: false,
					afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_CLACC_COM.TZ_JUDCLINFO_STD.password","密码"),
					inputType: 'password',
					name: 'password',
					allowBlank: false,
					afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_CLACC_COM.TZ_JUDCLINFO_STD.rePassword","确认密码"),
					inputType: 'password',
					name: 'rePassword',
					allowBlank: false,
					afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_CLACC_COM.TZ_JUDCLINFO_STD.judgeName","用户描述"),
					name: 'judgeName'
				}/*,{
					layout: {
						type: 'column',
					},	
					items:judTpItems
				}*/,{
                    xtype: 'textfield',
                    fieldLabel: "手机",
                    name: 'judgePhoneNumber',
                    regex: /^[1]\d{10}$/,
                    allowBlank: false,
        			afterLabelTextTpl: [
        	            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
        	        ]
                },{
                    xtype: 'textfield',
                    vtype:'email',
                    fieldLabel: "邮箱",
                    name: 'judgeEmail'
                },{
					xtype:'tagfield',
                    fieldLabel:'行业类别',
                    name:'judgeHY',
                    anyMatch:true,
                    filterPickList: true,
                    createNewOnEnter: true,
                    createNewOnBlur: false,
                    enableKeyEvents: true,
                    ignoreChangesFlag:true,
                    store: new KitchenSink.view.common.store.appTransStore("TZ_PWHY_ID"),
                    valueField: 'TValue',
            		displayField: 'TSDesc',
            		queryMode: 'remote',
            		listeners:{
                        'select': function(combo,record,index,eOpts)//匹配下拉值之后置空输入文字
                        {
                            var me = this;
                            me.inputEl.dom.value = "";
                        }
                    }
                    /*triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchListeners"
                        }
                    }*/
                },/*{
	                xtype: 'textfield',
	                fieldLabel: "角色描述",
	                name: 'roleNameDesc',
	                readOnly:true,
	                fieldStyle:'background:#F4F4F4'
	            },*//*{
	            	layout: {
	                    type: 'column'
	                },
	                items:[{
	                	columnWidth:.25,
	                	xtype: 'textfield',
	                    fieldLabel: '角色名称',
	        			// maxLength: 170,
	        			name: 'roleName',
	        			editable: false,
	                    triggers: {
	                        search: {
	                            cls: 'x-form-search-trigger',
	                            handler: "searchRoleName"
	                        }
	                    }
	                },{
	                	columnWidth:.75,
	                	xtype: 'displayfield',
	                    hideLabel: true,
	                    // fieldLabel: "角色描述",
	                    name: 'roleNameDesc',
	                    //readOnly:true,
	                    //fieldStyle:'background:#F4F4F4'
	                }]
	                
	            },{
                	xtype: 'checkbox',
                	fieldLabel: '用户类型',
                	boxLabel: '材料评委',
                	name: 'userType',
                	inputValue: "Y",
					checked: true,
					readOnly:true,
					hidden:true
                },*/{
					xtype:'combobox',
					fieldLabel:"院内/院外",
					editable:false,
					emptyText:'请选择',
					queryMode:'remote',
					name:'judgeYnyw',
					valueField:'TValue',
					displayField:'TSDesc',
					store:new KitchenSink.view.common.store.appTransStore("TZ_YNYW")
				},{
					xtype: 'checkboxgroup',
					fieldLabel: "用户类型",
					name:'userType',
					//fieldLabel: '评委类型',
					//id: 'judgeTypeCheckboxGroup',
					//itemId:'judgeTypeCheckboxGroup',
					vertical: true,
					items: judTpItems,
					allowBlank: false,
					columns: 4,
					labelWidth: 100,
                    //width: 750,
                    align: 'left',
                    anchor: '100%', 
                    flex: 1,
					afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
				},{
					layout: {
						type: 'column'
					},
					items:[{
						columnWidth:.35,
						xtype: 'displayfield',
//						fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.orgId","所属机构"),
						fieldLabel: Ext.tzGetResourse("TZ_JUDGE_CLACC_COM.TZ_JUDCLINFO_STD.orgId","所属机构"),
						name: 'orgId'
					},{
						columnWidth:.65,
						xtype: 'displayfield',
						hideLabel: true,
						name: 'orgDesc'
					}]
				},{
					xtype: 'checkbox',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_CLACC_COM.TZ_JUDCLINFO_STD.clockFlag","锁定用户"),
					name: 'clockFlag',
					inputValue: "Y"
				},{
                    xtype: 'textarea',
                    fieldLabel: '备注',
                    name: 'beizhu'
                    //fieldStyle:'background:#F4F4F4'
                }/*,{
		    		xtype: 'combobox',
		            fieldLabel: "账号类型",
		            editable:false,
		            emptyText:'请选择',
		            queryMode: 'remote',
		    	    	name: 'rylx',
		    	    	valueField: 'TValue',
		        		displayField: 'TSDesc',
		        		store: new KitchenSink.view.common.store.appTransStore("TZ_RYLX"),
		        		afterLabelTextTpl: [
		                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
		            ],
		            allowBlank: false,
		            listeners:{
		                	afterrender: function(combox){
		                		//当前登录人机构id
		    	            	combox.readOnly = true;
		    	            }
		    	          }
		    	          
		        }*/]
			}]
		})
	 this.callParent();
    },
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onJudgeAccSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onJudgeAccEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onWinClose'
	}]
	
});
