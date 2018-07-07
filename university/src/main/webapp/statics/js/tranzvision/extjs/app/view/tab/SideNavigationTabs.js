Ext.define('KitchenSink.view.tab.SideNavigationTabs', {
	extend: 'Ext.tab.Panel',
	xtype: 'side-navigation-tabs',
	config: {
		fields: undefined
	},
	isLoaded: false,
	predefinetpl: '',
	height: 400,
	width: 800,
	ui: 'navigation',
	tabPosition: 'left',
	tabRotation: 0,
	bodyPadding: 15,
	tabBar: {
		// turn off borders for classic theme.  neptune and crisp don't need this
		// because they are borderless by default
		border: false
	},
	defaults: {
		textAlign: 'left'
	},
	items: '',
	initComponent: function() {

		var lanStore = new KitchenSink.view.common.store.appTransStore("TZ_GD_LANGUAGE"); //语言
		Ext.apply(this, {
			items: [
				{
					itemId: 'add',
					title: '新建空白模板',
					xtype: 'form',
					fieldDefaults: {
						msgTarget: 'side',
						labelWidth: 100,
						labelStyle: 'font-weight:bold'
					},
					items: [
						{
							layout: 'column',
							items: [
								{
									xtype: 'textfield',
									fieldLabel: "模板名称",
									name: 'modelName',
									cls: 'bmb_blank_text',
									allowBlank: false,
									blankText: "模板名称必填！",
									columnWidth: .5
								},
								{
									xtype: 'combo',
									forceSelection: true,
									valueField: 'TValue',
									displayField: 'TSDesc',
									name: 'language',
									mode: "remote",
									store: lanStore,
									columnWidth: .2,
									editable: false,
									style: {
										marginLeft: '25px'
									},
									value: 'ZHS'
								}
							]
						},
						{
							html: '<div>空白模板描述</div>'
						}
					]
				},
				{
					itemId: 'predefine',
					title: '使用预定义模板',
					xtype: 'form',
					fieldDefaults: {
						msgTarget: 'side',
						labelWidth: 100,
						labelStyle: 'font-weight:bold'
					},
					items: [
						{
							layout: 'column',
							items: [
								{
									xtype: 'textfield',
									fieldLabel: "模板名称",
									value: '',
									labelStyle: 'font-weight:bold',
									cls: 'bmb_predefine_text',
									allowBlank: false,
									blankText: "模板名称必填！",
									columnWidth: .8
								}
							]
						},
						{
							html: '<div class="predefinetpllist" style="overflow-y:auto;height:350px;">' + this.getPredefinetpl() + "</div>"
						}
					]
				}
			]
		});
		this.callParent();
	},
	getPredefinetpl: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_OTHER_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert("提示", "您没有修改数据的权限");
			return;
		}

		var me = this,
			predefinetpl = '';
		if (!me.isLoaded) {
			var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"YDYTPL"}}';

			Ext.Ajax.request({
				url: Ext.tzGetGeneralURL,
				async: false,
				params: {
					tzParams: tzParams
				},
				waitTitle: '请等待',
				waitMsg: '正在加载中',
				success: function(response) {
					var jsonText = response.responseText;
					try {
						var jsonObject = Ext.util.JSON.decode(jsonText);
						/*判断服务器是否返回了正确的信息*/
						if (jsonObject.state.errcode === 0) {
							var resText = jsonObject.comContent;
							//var responseData = Ext.JSON.decode(resText)
							var responseData = resText;
							for (var i in responseData) {
								predefinetpl += '<div class="tplitem" style="padding: 10px;cursor: pointer;border: 1px solid #eee;display: inline-table;margin: 5px;text-align:center;width:176px;" onclick="mybmb_cj(this)" data-id="' + responseData[i].tplid + '"><img src="' + TzUniversityContextPath + '/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png"><br><span class="tplname" title="' + responseData[i].tplname + '">' + Ext.String.ellipsis(responseData[i].tplname, 16, true) + '</span></div>';
							}
							me.isLoaded = true;
						} else {
							TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);
						}
					} catch(e) {
						TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
					}
				},
				failure: function(response, opts) {
					//错误信息响应报文
					var respText = Ext.util.JSON.decode(response.responseText);
					TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
				}
			});
		}
		return predefinetpl;
	}
});