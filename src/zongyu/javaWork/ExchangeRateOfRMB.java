package zongyu.javaWork;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.script.ScriptContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellElapsedFormatter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlDateInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.istack.internal.FinalArrayList;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMLeaf;

import javafx.scene.control.Cell;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.DoLoop;

public class ExchangeRateOfRMB
{
	public static void main(String[] args)
	{
		// 这个URL是单独数据的URL 是嵌入在http://www.safe.gov.cn/wps/portal/sy/tjsj_hlzjj_inquire
		// 中的iframe
		String url = "http://www.safe.gov.cn/AppStructured/view/project!RMBQuery.action";
		String filePath = "F:\\workspace\\自学作业\\out\\workbook.xls";
		Html html = new Html();
		OutToExcel outToExcel = new OutToExcel();
		if (outToExcel.WriteFile(filePath, html.getContext(url)))
		{
			System.out.println("成功写入文件！");
		} else
		{
			System.out.println("写入文件失败！");
		}

	}
}

	class Html
	{
		public ArrayList<ArrayList<String>> getContext(String url)
		{
			ArrayList<ArrayList<String>> text = new ArrayList<ArrayList<String>>();
			WebClient webClient = new WebClient(BrowserVersion.CHROME); // 实例化Web客户端
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setActiveXNative(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.waitForBackgroundJavaScript(10 * 1000);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			try
			{
				HtmlPage page = webClient.getPage(url); // 解析获取页面
				HtmlForm form = page.getFormByName("RMBQuery");
				if (form == null)
				{
					System.out.println("get form fail!");
				}
				//
				HtmlTextInput startTime = form.getInputByName("projectBean.startDate"); // 获取查询文本框
				HtmlTextInput endTime = form.getInputByName("projectBean.endDate"); // 获取查询文本框
				HtmlButtonInput b1 = form.getInputByValue("查询"); // 获取查询按钮
				startTime.setValueAttribute("2017-11-13");
				endTime.setValueAttribute("2017-12-13");
				HtmlPage ansPage = b1.click(); // 模拟点击

				// 得到点击后的页面
				HtmlTable table = ansPage.getHtmlElementById("InfoTable");

				// 获取美元(col=1)，欧元(col=2)，港币(col=4), 还有日期
				int[] nations = { 0, 1, 2, 4 };
				for (int i = 0; i < table.getEndLineNumber(); i++)
				{
					ArrayList<String> row = new ArrayList<>();
					for (int j = 0; j < nations.length; j++)
					{
						// System.out.print(table.getCellAt(i, nations[j]).asText()+" ");
						HtmlTableCell cell = table.getCellAt(i, nations[j]);
						if (cell != null)
						{
							// System.out.println(cell.getId()+" "+ cell.asText()+" "+ cell.asXml());
							String td = cell.asText();
							row.add(td);
						}
					}
					text.add(row);
				}
			} catch (FailingHttpStatusCodeException e)
			{
				e.printStackTrace();
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				webClient.close(); // 关闭客户端，释放内存
			}
			return text;
		}
	}

	class OutToExcel
	{
		public boolean WriteFile(String filePath, ArrayList<ArrayList<String>> text)
		{
			// 创建HSSFWorkbook对象
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建HSSFSheet对象
			HSSFSheet sheet = wb.createSheet("sheet0");
			for (int i = 0; i < text.size(); i++)
			{
				ArrayList<String> rowtext = text.get(i);
				// 创建HSSFRow对象
				HSSFRow row = sheet.createRow(i);
				for (int j = 0; j < rowtext.size(); j++)
				{
					// 创建HSSFCell对象
					HSSFCell cell = row.createCell(j);
					// 设置单元格的值
					cell.setCellValue(rowtext.get(j));
				}
			}
			try
			{
				// 输出Excel文件
				FileOutputStream output = new FileOutputStream(filePath);
				wb.write(output);
				output.flush();
				return true;
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return false;

		}
	}


