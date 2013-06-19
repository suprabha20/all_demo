package com.eoeandorid.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Environment;

public class EpubKernel {
	
	// xml parser
	private DocumentBuilderFactory mDocumentBuilderFactory;
	private DocumentBuilder mDocumentBuilder;
	
	// epub基本目录(每个epub解压后的基本目录)
	private String epubBaseDir = "";
	
	// opf
	private List<String> opfList;
	
	// metadata
	private Map<String,String> metadataMap;
	// manifestitme
	private Map<String,EpubManifestItem> manifestItemMap;
	// spine
	private Map<String,String> spineMap;
	
	private String ncx;
	
	private static final String CONTAINER_FILE = "/META-INF/container.xml";
	private static final String CONTAINER_FILE_MEDIA_TYPE = "application/oebps-package+xml";
	private static final String EPUB_ROOT_PATH = Environment.getExternalStorageDirectory().getPath()+"/EoeReader/epub/";
	
	// 初始化APP的epub工作目录
	public void init() throws ParserConfigurationException{
		
		File file = new File(EPUB_ROOT_PATH);
		if(!file.exists()){
			file.mkdirs();
		}
		
		mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
	}
	
	public void openEpubFile(String epubFilePath,String epubWorkDiectory) throws FileNotFoundException,IOException
				,SAXException,ParserConfigurationException{
		init();
		unzipEpub(epubFilePath,epubWorkDiectory);
		this.epubBaseDir = epubWorkDiectory;
		parserEpubFile(epubWorkDiectory);
	}
	
	// 解压epub到workDiectory目录�?workDiectory的命名规则为BOOKID)
	public void unzipEpub(String epubFilePath,String epubWorkDiectory){
		try {
			ZipUtil.unzipEpub(epubFilePath, epubWorkDiectory);
		} catch (Exception e) {
		}
	}
	
	// 解析解压后的epub目录
	public void parserEpubFile(String epubUnzipDiectory) throws FileNotFoundException,IOException,SAXException{
		File file = new File(epubUnzipDiectory);
		if(!file.exists()){
			return;
		}
		parseContainer();
		parseOpfFile();
		//prrseNcxFile();
	}
	
	private void parseContainer() throws FileNotFoundException,IOException,SAXException{
		String containerFile = this.epubBaseDir + CONTAINER_FILE;
		System.out.println("container file: "+containerFile);
		File file = new File(containerFile);
		FileInputStream fis = new FileInputStream(file);
		Document document = mDocumentBuilder.parse(fis);
		
		String fullPath = "";
		String mediaType = "";
		Element rootNode = (Element)document.getElementsByTagName("rootfiles").item(0);
		NodeList nodes = rootNode.getChildNodes();
		if(nodes.getLength() > 0){
			opfList = new ArrayList<String>();
			for (int i = 0; i < nodes.getLength(); i++) {
				fullPath = DomUtil.getElementAttr(document,"rootfile","full-path",i);
				mediaType = DomUtil.getElementAttr(document,"rootfile","media-type",i);
				if (mediaType.equals(CONTAINER_FILE_MEDIA_TYPE)){
					opfList.add(fullPath);
					break;
				}
			}
		}
	}
	
	private void parseOpfFile() throws FileNotFoundException,IOException,SAXException{
		if(opfList == null || opfList.size() <= 0){
			return;
		}
		String opfFile = this.epubBaseDir + "/" + opfList.get(0);
		System.out.println("opfFile: "+opfFile);
		File file = new File(opfFile);
		InputStream is = new FileInputStream(file);
		Document document = mDocumentBuilder.parse(is);
		// 获取opf文件中metadata相关数据(图书基础信息)
		metadataMap = new HashMap<String,String>();
		metadataMap.put("title", DomUtil.getElementValue(document, "dc:title", 0));
		metadataMap.put("creator", DomUtil.getElementValue(document, "dc:creator", 0));
		metadataMap.put("description", DomUtil.getElementValue(document, "dc:description", 0));
		
		// 获取opf文件中manifest相关数据(资源清单)
		Element manifestNode = (Element)document.getElementsByTagName("manifest").item(0);
		NodeList nodes = manifestNode.getChildNodes();
		if(nodes.getLength() > 0){
			manifestItemMap = new HashMap<String,EpubManifestItem>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if ( node.getNodeType() == Node.ELEMENT_NODE ){
					Element elManifestItem = (Element)node;
					EpubManifestItem item = new EpubManifestItem();
					item.itemId = elManifestItem.getAttribute("id");
					item.href = elManifestItem.getAttribute("href");
					item.mediaType = elManifestItem.getAttribute("media_type");
					manifestItemMap.put(item.itemId, item);
				}
			}
		}
		// 获取opf文件中spine相关数据(阅读顺序)
		Element spineNode = (Element)document.getElementsByTagName("spine").item(0);
		// 获取spine中的toc属�?(之后就能根据该属性在manifestItemMap中获取href属�?对应的文件路�?
		ncx = spineNode.getAttribute("toc");
		
		NodeList spines = document.getElementsByTagName("itemref");
		if(spines.getLength() > 0){
			spineMap = new HashMap<String,String>();
			for (int i = 0; i < spines.getLength(); i++) {
				String idref = DomUtil.getElementAttr(document, "itemref", "idref", i);
				spineMap.put(String.valueOf(i), idref);
			}
		}
	}
	
	private void prrseNcxFile() throws FileNotFoundException,IOException,SAXException{
		
	}
	
	public Map<String, String> getSpineMap() {
		return spineMap;
	}
	
	public String getHtmlUrlByIndex(int index){
		String pageIndex = spineMap.get(String.valueOf(index));
		EpubManifestItem item = manifestItemMap.get(pageIndex);
		return "file:///"+this.epubBaseDir + "/" + item.href;
	}
	
	private class EpubManifestItem{
		public EpubManifestItem(){}
		public String itemId;
		public String href;
		public String mediaType;
	}
}
