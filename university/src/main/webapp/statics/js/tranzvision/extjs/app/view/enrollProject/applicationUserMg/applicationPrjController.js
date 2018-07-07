Ext.define('KitchenSink.view.enrollProject.applicationUserMg.applicationPrjController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.appPrjController',
    //查询
    queryPrj:function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_BMGL_APPL_COM.TZ_BMGL_PRJ_STD.TZ_PRJ_TYPE_T',
			condition:
            {
                "TZ_JG_ID": Ext.tzOrgID
            }, 
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
    },
	viewAppUser:function(grid,html,rowIndex,colIndex,table,record){
          var prjID = record.data.prjID;
          var contentPanel, className, ViewClass, cmp,bugID;
          var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_APPL_COM"]["TZ_UM_USERMG_STD"];
          if( pageResSet == "" || pageResSet == undefined){
              Ext.MessageBox.alert('提示', '您没有修改数据的权限');
              return;
          }
          //该功能对应的JS类
          className = pageResSet["jsClassName"];
          contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
          contentPanel.body.addCls('kitchensink-example');
          if (!Ext.ClassManager.isCreated(className)) {
              Ext.syncRequire(className);
          }
          var render = function(initialData){
		    	var ViewClass =Ext.ClassManager.get('KitchenSink.view.enrollProject.applicationUserMg.appUserMgList');
		    	config={initialData:initialData,
		    			prjID:prjID	
		      	     };	
		 		var cmp =new ViewClass(config);
		 		
		 		cmp.on('afterrender',function(panel){
		 			var store = panel.getStore();
		
		 	        	var tzStoreParams = {
		 	            		"cfgSrhId": "TZ_BMGL_APPL_COM.TZ_UM_USERMG_STD.TZ_APP_USER_VW",
		 	            		"condition":{
		 	            			"TZ_JG_ID-operator": "01",
		 	            			"TZ_JG_ID-value":Ext.tzOrgID,
			            			"TZ_PRJ_TYPE_ID-operator":"01",
			            			"TZ_PRJ_TYPE_ID-value":prjID
		 	            				}
		 	            };
		 	        store.tzStoreParams=Ext.encode(tzStoreParams);
		 	        store.reload();
		 			
		 		});
		 		tab = contentPanel.add(cmp);
		 		contentPanel.setActiveTab(tab);
		        Ext.resumeLayouts(true);
		        if (cmp.floating) {
		            cmp.show();
		        }
          };
          var  orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
              recname:'TZ_ORG_COLOR_V',
              condition:{
                  TZ_JG_ID:{
                      value:Ext.tzOrgID,
                      operator:'01',
                      type:'01'
                  }},
              result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
          });
      
      //下拉项过滤器数据
      var colorSortFilterOptions=[];
      
      //颜色类别初始化数据-学生颜色类别列渲染数据
      var initialColorSortData=[];
      
      //5个下拉控件Store加载完毕之后打开页面
      var times = 1;
      var beforeRender = function(){
      	times--;
	        if(times==0){
	        	render({
	        		orgColorSortStore:orgColorSortStore,
	        		colorSortFilterOptions:colorSortFilterOptions
	        	});
	        }
	    };
	    
      orgColorSortStore.on("load",function(store, records, successful, eOpts){
      	for(var i=0;i<records.length;i++){
          	initialColorSortData.push(records[i].data);
              colorSortFilterOptions.push([records[i].data.TZ_COLOR_SORT_ID,records[i].data.TZ_COLOR_NAME]);
          }
          beforeRender();
	    });
    }
});

