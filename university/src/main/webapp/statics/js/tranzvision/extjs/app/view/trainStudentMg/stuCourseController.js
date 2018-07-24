Ext.define('KitchenSink.view.trainStudentMg.stuCourseController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.stuCourseController', 
    searchComList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'PX_STU_COURSE_COM.PX_STU_COURSE_STD.PX_STU_COURSE_V',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},   
	
	closeComRegInfos: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	}
});