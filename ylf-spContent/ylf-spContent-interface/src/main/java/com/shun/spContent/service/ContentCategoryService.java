package com.shun.spContent.service;

import java.util.List;
import com.shun.common.pojo.EasyUiTreeNode;
import com.shun.common.pojo.ylfResult;

/**
* @author czs
* @version 创建时间：2018年5月19日 上午11:36:24
* 用于网站内容分类的服务接口 
*/
public interface ContentCategoryService {

	/**
	 * 根据父id获取所有的子节点</br>
	 *[{                                         </br>
	 *    "id": 1,                               </br>
	 *    "text": "Node 1",                      </br>
	 *    "state": "closed",                     </br>
	 *    "children": [{                         </br>
	 *        "id": 11,                          </br>
	 *        "text": "Node 11"                  </br>
	 *    },{                                    </br>
	 *        "id": 12,                          </br>
	 *        "text": "Node 12"                  </br>
	 *    }]                                     </br>
	 *},{                                        </br>
	 *    "id": 2,                               </br>
	 *    "text": "Node 2",                      </br>
	 *    "state": "closed"                      </br>
	 *}]                                         </br>
	 * @param parentCategoryId 父id
	 * @return 由于需要返回的是这种格式，所以需要返回一个EasyUiTreeNode的List结合返回给页面
	 */
	public List<EasyUiTreeNode> getContentCatTreeNodes(long parentCategoryId);

	/**
	 * 添加一个网站内容分类
	 * @param parentId 设置分类的父分类
	 * @param name 分类的名称
	 * @return 返回处理结果类
	 */
	public ylfResult addContentCategory(long parentId, String name);

	/**
	 * 修改网站分类节点名称
	 * @param id
	 * @param name
	 */
	public void updateCategoryNameById(long id, String name);

	/**
	 * 通过节点id来删除节点
	 * @param id
	 * @return 
	 */
	public ylfResult delete(long id);
}
