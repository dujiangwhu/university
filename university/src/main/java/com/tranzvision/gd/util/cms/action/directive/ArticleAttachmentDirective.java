package com.tranzvision.gd.util.cms.action.directive;

import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_ATTLIST;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.entity.main.Attachment;
import com.tranzvision.gd.util.cms.manager.main.ArticleMng;
import com.tranzvision.gd.util.cms.manager.main.ArticleMngImpl;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ArticleAttachmentDirective implements TemplateDirectiveModel {
	/**
	 * 输入参数，文章ID。
	 */
	public static final String PARAM_ID = "id";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		ArticleMng articleMng = new ArticleMngImpl();

		String id = DirectiveUtils.getString(PARAM_ID, params);
		List<Attachment> list = new ArrayList<Attachment>();
		if (StringUtils.isBlank(id)) {
			return;
		}
		list = articleMng.findArticleAttachmentsById(id);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);

		paramWrap.put(OUT_ATTLIST, DEFAULT_WRAPPER.wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

}
