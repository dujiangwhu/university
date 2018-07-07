Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'jugClMgInfo',
    controller: 'jugClMg',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],
    title: '评审组定义',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent: function() {
        var classStore = new KitchenSink.view.common.store.comboxStore({
            recname: 'PS_TZ_JUGTYP_TBL',
            condition: {
                TZ_JG_ID: {
                    value: Ext.tzOrgID,
                    operator: '01',
                    type: '01'
                },
                TZ_JUGTYP_STAT: {
                    value: '0',
                    operator: '01',
                    type: '01'
                }
            },
            result: 'TZ_JUGTYP_ID,TZ_JUGTYP_NAME'
        });


        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'jugClGroupForm',
                actType: '',
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                //heigth: 600,

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    fieldLabel: '评审组ID',
                    maxLength: 150,
                    name: 'jugGroupId',
                    readOnly: true,
                    fieldStyle: 'background:#F4F4F4'
                }, {
                    xtype: 'textfield',
                    fieldLabel: '评审组名称',
                    maxLength: 125,
                    name: 'jugGroupName',
                    allowBlank: false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    fieldLabel: '评审组类型',
                    xtype: 'combobox',
                    // allowBlank:false,
                    // reference: '',
                    name: 'jugGroupType',
                    // style: 'margin-top:10px',
                    store: classStore,
                    valueField: 'TZ_JUGTYP_ID',
                    displayField: 'TZ_JUGTYP_NAME',
                    queryMode: 'lcoal',
                    allowBlank: false,
                    editable: false,
                    columnWidth: .8,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }]
            }],
            buttons: [{
                text: '保存',
                iconCls: "save",
                handler: 'onFormSave'
            }, {
                text: '确定',
                iconCls: "ensure",
                handler: 'onFormEnsure'
            }, {
                text: '关闭',
                iconCls: "close",
                handler: 'onFormClose'
            }]
        });
        this.callParent();
    }
});