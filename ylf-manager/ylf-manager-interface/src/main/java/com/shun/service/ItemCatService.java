package com.shun.service;

import java.util.List;
import com.shun.common.pojo.EasyUiTreeNode;

/**
* @author czs 这是为商品分类而准备的接口
* @version 创建时间：2018年5月12日 上午11:38:31 
*/
public interface ItemCatService {

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
	 *}]                                         </br>		由于需要返回的是这种格式，所以需要返回一个EasyUiTreeNode的List结合返回给页面
	 * @param parentId 父id
	 * @return 返回所有符合的子节点
	 */
	public List<EasyUiTreeNode> getItemCatListByNodeParentId(long parentId);
}
  