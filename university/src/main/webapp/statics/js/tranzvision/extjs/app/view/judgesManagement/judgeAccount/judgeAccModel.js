Ext.define('KitchenSink.view.judgesManagement.judgeAccount.judgeAccModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'accountNo'},//评委账号
        {name: 'judgeName'},//评委名称
        {name: 'judgeType'},//评委类型
        {name: 'judgeTypeDesc'},//评委类描述
		{name: 'judTypeDesc'},//评委类型名称
		{name: 'oprId'},
		{name: 'orgId'},
		{name: 'judgeHY'},//行业类别
		{name: 'judgeHYID'},//行业类别
        {name: 'judPhoneNumber'},//评委手机号
        {name: 'judEmail'},//评委邮箱
        {name: 'clpwNum'}
//        {name: 'judOAID'},//评委OA账号
//        {name: 'judDepart'}//评委所属系


	]
});
