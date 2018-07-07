Ext.define('KitchenSink.view.template.bmb.myBmbPdfModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tplID'},
        {name: 'fieldID'},
        {name: 'fieldName'},
        {name: 'pdffield1'},
        {name: 'pdffield2'},
        {name: 'pdffield3'},
        {name: 'isImg'}
    ],
    formatCheckbox:function(){
		this.set("isImg",this.get("isImg")=="Y");
		this.commit();
	}
});
