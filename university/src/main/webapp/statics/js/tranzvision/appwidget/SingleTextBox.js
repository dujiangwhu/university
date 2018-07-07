SurveyBuild.extend("SingleTextBox", "baseComponent", {
	itemName: "单行文本框",
	title: "单行文本框",
	isCheckStrLen: "N",
	"StorageType": "S",
	minLen: "0",
	maxLen: "200",

	_getHtml: function(data, previewmode) {
		var c = "";

		if (previewmode) {
			if(SurveyBuild.accessType == "M"){
				if (SurveyBuild._readonly) {
					//只读模式
					c += '<div class="item">';
					c += '<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
					c += '<div class="text-box"><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="text1" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '"/></div>';
					c += '<p style="color:#666;font-size:0.56rem;"></p>';
					c += '</div>';
				} else {
					//填写模式
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					if (data.isReadOnly == "Y" && data.value=="") {
						this._getDefaultVal(data);
					}
					
					var regular = "";
					if (data.preg && SurveyBuild._preg.hasOwnProperty(data.preg)) {
						regular = SurveyBuild._preg[data.preg]["regExp"];
					}
					c += '<div class="item">';
					c += '<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
					c += '<div id="' + data.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';					
					c += '<div class="text-box"><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="text1" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '"/></div>';
					if ($.trim(data.onShowMessage) != "") {
						c += '<p style="color:#666;font-size:0.56rem;">' + data.onShowMessage + '</p>';
					}
					c += '</div>';
					}
				} else{
				if (SurveyBuild._readonly) {
					//只读模式
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '  <div class="input-list-text left">' + data.value + '</div>';
					c += '  <div class="input-list-suffix left"></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';
				} else {
					//填写模式
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					
					if (data.isReadOnly == "Y"  && data.value=="") {
						this._getDefaultVal(data);
					}
					
					var regular = "";
					if (data.preg && SurveyBuild._preg.hasOwnProperty(data.preg)) {
						regular = SurveyBuild._preg[data.preg]["regExp"];
					}

					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '  <div class="input-list-text left"><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="inpu-list-text-enter" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '"/></div>';
					c += '  <div class="input-list-suffix left">' + (data["suffix"] ? data.suffix + '<span class="input-list-suffix-span">&nbsp;</span>': "") + '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';

					if ($.trim(data.onShowMessage) != "") {
						c += '<div class="input-list-blank" id="' + data.itemId + 'msg">';
						c += '	<div class="input-list-info-blank left"><span class="red-star"></div>';
						c += '	<div class="input-list-wz left"><span class="blue">' + data.onShowMessage + '</span></div>';
						c += '	<div class="input-list-suffix-blank left"></div>';
						c += '	<div class="clear"></div>';
						c += '</div>';
					}
				}
			}
		} else {
			c += '<div class="question-answer">';
			c += '  <div class="format">';
			c += '      <b class="read-input"></b>';
			c += '      <span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
			c += '  </div>';
			c += '</div>'
		}

		return c;
	},
	_edit: function(data) {
		var _fix = "",
		e = "";

		$.each(SurveyBuild._preg,
		function(reg, regObj) {
			_fix += '<option value="' + reg + '"' + (data.preg == reg ? ' selected="selected"': "") + ">" + regObj["name"] + "</option>"
		});
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">提示信息：</span>';
		e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'onShowMessage\')" value="' + data.onShowMessage + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
		e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">默认值：</span>';
		e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp" style="text-align:right;">';
		e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
		e += '</div>';
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">关联项：</span>';
		e += '  <input type="text" class="medium" id="linkItems" onkeyup="SurveyBuild.saveAttr(this,\'linkItems\')" value="' + data.linkItems + '"/>';
		e += '</div>';
		
		e += '<div class="edit_paramSet mt10"><span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '<div class="groupbox">';
		e += '<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isReadOnly\')"' + (data.isReadOnly == "Y" ? "checked='checked'": "") + ' id="is_readOnly"> <label for="is_readOnly">是否只读</label></div>';
		e += '<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isFirstLetterUpper\')"' + (data.isFirstLetterUpper == "Y" ? "checked='checked'": "") + ' id="isFirstLetterUpper"> <label for="isFirstLetterUpper">是否首字母大写</label></div>';
		e += '</div>';
		e += '</div>';

		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
		e += '  <div class="edit_item_warp" style="margin-top:5px;">';
		e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '  </div>';

		e += '  <div class="edit_item_warp">';
		e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isCheckStrLen\')"' + (data.isCheckStrLen == "Y" ? "checked='checked'": "") + ' id="is_checkstrlen"> <label for="is_checkstrlen">字数范围</label>';
		e += '  </div>';

		e += '  <div class="edit_item_warp">';
		e += '      <span class="edit_item_label">最少：</span>';
		e += '      <input type="text" maxlength="11" class="medium minLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minLen\')" value="' + data.minLen + '"/>';
		e += '  </div>';

		e += '  <div class="edit_item_warp">';
		e += '      <span class="edit_item_label">最多：<a href="#" data-for-id="help_maxLen" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
		e += '      <input type="text" maxlength="11" class="medium maxLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxLen\')" value="' + data.maxLen + '"/>';
		e += '  </div>';

		e += '  <div class="edit_item_warp mb10">';
		e += '      <span class="edit_item_label" >校验规则：</span>';
		e += '      <select id="is_preg" onchange="SurveyBuild.saveAttr(this,\'preg\')">';
		e += '          <option></option>' + _fix;
		e += '      </select>';
		e += '  </div>';

		e += '  </div>';

		e += '  <div class="edit_item_warp">';
		e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '  </div>';
		e += '</div>';

		return e;
	},
	_validatorAttr: function(data) {
		var msg;

		var maxLen = parseInt(data.maxLen);
		if (maxLen > 254) {
			msg = "字数最多不能超过254个字符！";
			var $targetObj = $(".maxLen");
			SurveyBuild.fail($targetObj, msg);
			return false;
		}
		return true;
	},
	_eventbind: function(data) {
		var $inputBox = $("#" + data.itemId);

		$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "&nbsp;",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		$inputBox.functionValidator({
			fun: function(val, elem) {

				//执行高级设置中的自定义规则
				/*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],
					function(classname, classObj) {
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"], classObj["messages"], data);
								if (_result !== true) {
									return false;
								}
							}
						}
					});
					if (_result !== true) {
						return _result;
					}
				}
				return _result;
			}
		});
		/*关联项*/
		if(data.linkItems){
			var $linkitems = $("#" + data.linkItems);
			if($linkitems && $linkitems.length > 0){
				$inputBox.change(function(e) {
					$linkitems.val($inputBox.val());
					$linkitems.trigger("change");
					$linkitems.trigger("blur");
				});
			}
		}
		//设置首字母大写
		if(data.isFirstLetterUpper == "Y"){
			$inputBox.change(function(e) {
				var inputVal = $inputBox.val();
				inputVal = inputVal.slice(0, 1).toUpperCase() + inputVal.slice(1);
				$inputBox.val(inputVal);
			});
		}
	}
});