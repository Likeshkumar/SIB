<%@taglib uri="/struts-tags" prefix="s" %>

<s:iterator  value="scratchcardbeans.productList">
			<tr>
				<td>Product Description : </td>
				<td><textarea name="productDesc" id="productDesc" maxlength="80" 
				 style="resize: none;max-width: 200px; max-height: 70px;">${SCHPROD_DESC}</textarea></td>
			</tr>
</s:iterator>

