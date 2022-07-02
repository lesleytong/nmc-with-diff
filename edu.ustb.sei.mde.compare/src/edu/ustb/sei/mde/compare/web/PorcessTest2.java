package edu.ustb.sei.mde.compare.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.NoMergeBaseException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.transport.*;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import edu.ustb.sei.mde.compare.EMFCompare;
import edu.ustb.sei.mde.compare.match.IMatchEngine;
import edu.ustb.sei.mde.compare.match.impl.MatchEngineFactoryImpl;
import edu.ustb.sei.mde.compare.match.impl.MatchEngineFactoryRegistryImpl;
import edu.ustb.sei.mde.compare.myconflict.MyconflictPackage;
import edu.ustb.sei.mde.compare.nmc.NWay;
import edu.ustb.sei.mde.compare.utils.UseIdentifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;

public class PorcessTest2 {

    public static final String baseUrl = "http://101.43.149.150:8080";
    public static final String apiUrl = baseUrl + "/gitserver/";




    @Test
    public void testProcess() throws Exception {

        String projectName = "new_project";
        String localString = "C:\\Users\\10242\\Desktop\\newp";

        // 项目发起者创建项目。
        // 同时，初始化的本地仓库默认在 master 分支下。
//        createProject("lyt", "lyt12345", projectName, localString);

        // 项目发起者查看所参与项目的信息
//        getProjects("lyt", "lyt12345");

//        String projectId = "71";
//        String remoteString = "http://101.43.149.150/lyt/new_project.git";

        // 服务器部署好后，可以省略
//        String latestCommit = getLatestCommit(localString);
//        addVersion(projectId, latestCommit);

//        // 再根据 projectId 去查看项目的所有版本号
//        findAllVersions(projectId);   // 1.0






        // 项目发起者在本地 master 分支进行工作，再提交到远程库。
//        String message = "add bank.ecore lyt";
//        String branchName = "master";
//        MyGit.commitAndPush(localString, message, branchName);

        // 服务器部署好后，可以省略
//        String latestCommit = getLatestCommit(localString);
//        addVersion(projectId, latestCommit);

        // 检查版本号是否自增
//        findAllVersions(projectId);   // 1.0 和 1.1






        // 项目发起者创建本地分支，并自动切换到了新分支
//        String branchName = "branch-lyt";
//        createBranch(localString, branchName);

        // 项目发起者在本地分支进行工作，再提交到远程库。
//        String message = "modify file1 lyt";
//        String branchName = "branch-lyt";
//        MyGit.commitAndPush(localString, message, branchName);

        // 服务器部署好后，可以省略
//        String latestCommit = getLatestCommit(localString);
//        addVersion(projectId, latestCommit);

        // 项目发起者检查版本号是否自增
//        findAllVersions(projectId);   // 最新是1.2








        // 项目发起者添加第 1 个项目成员
//        addMemmber("lyt", "lyt12345", "lesley", projectId, "30");

        // 项目成员查看所参与的项目号
//        getProjects("lesley", "lesley1234");

        // 获取远程仓库地址后，再查看项目有哪些远程分支
//        branchListRemote("lesley", "lesley1234", remoteString);

        // 项目成员shallowClone远程分支
        String local_lesley = "C:\\Users\\10242\\Desktop\\newpp";

//        String branchName = "master";   // 选择浅克隆的分支
//        MyGit.shallowCloneDepth1(local_lesley, branchName, remoteString);





        // 项目成员提交到非自己创建的分支
        // 注意，浅克隆的提交用 MyGit 中的
//        String message = "add file2 lesley";
//        String branchName = "master";
//        MyGit.commitAndPush(local_lesley, message, branchName);

        // 服务器部署好后，可以省略
//        String latestCommit = getLatestCommit(local_lesley);
//        addVersion(projectId, latestCommit);

        // 查看最新的版本
//        findLastedVersion(projectId);   // 最新是1.3








        // 项目发起者添加第 2 个项目成员
//        addMemmber("lyt", "lyt12345", "alice", projectId, "30");

        // 项目成员查看所参与的项目
//        getProjects("alice", "alice1234");

        // 获取远程仓库地址后，再查看项目有哪些远程分支
//        branchListRemote("alice", "alice1234", remoteString);

        // 项目成员shallowClone远程项目
        String local_alice = "C:\\Users\\10242\\Desktop\\newppp";

//        String branchName = "branch-lyt";   // 选择浅克隆的分支
//        MyGit.shallowCloneDepth1(local_alice, branchName, remoteString);

        // 项目成员创建新的分支，并自动切换到该分支
//        createBranch(local_alice, "branch-alice");

        // 项目成员提交到远程库
//        String message = "add file3 alice";
//        String branchName = "branch-alice";
//        MyGit.commitAndPush(local_alice, message, branchName);

        // 服务器部署好后，可以省略
//        String latestCommit = getLatestCommit(local_alice);
//        addVersion(projectId, latestCommit);

        // 查看最新的版本
//        findAllVersions(projectId);   // 最新是1.4







        // 若项目发起者此时还想回到 master 分支，则需要 checkOut
//        checkOut(localString, "master");

        // 可以先查看远程库的 master 分支是否有更新
//        checkUpdate(localString, "lyt", "lyt12345", projectId, "master");

        // 项目发起者也想提交到 master 分支，但远程库已有更新，因此会出现 Non-Fast-Forward 问题
        // 项目发起者首先需要pull
//        pullRepo("lyt", "lyt12345", localString);





    }

    @Test
	    public void testServerAPI2() throws IOException, GitAPIException, URISyntaxException {
	
	        String projectId = "71";
	
	        // 根据模板创建项目
	//        forkProject("alice", "alice1234", projectId, "testfork");
	
	        // 根据版本号查询提交
	//        findByVersion(projectId, "1.0");
	
	
	        // 根据SHA查询版本号
	//        String commitSHA = "d60bc12a952012f9f93b0894d3f83f9ab7087302";
	//        findVersionBySHA(projectId, commitSHA);
	
	        // 查询项目内的全部版本
	//        findAllVersions(projectId);
	
	        // 查询项目内的最新版本
	//        findLastedVersion(projectId);
	
	        // 项目发起者插入修改记录到黑板
	//        JSONObject params = new JSONObject();
	//        params.put("id", "12");
	//        params.put("type", "delete");
	//        insertLog("lyt", "lyt12345", projectId, params.toString());
	
	        // 查看全部记录
	//        findAllLogs("lyt", "lyt12345", projectId);
	
	        // 绑定修改记录
	//        bindLog(projectId, "6", "a99be7a1ba9d4c53ce9910047e69b5428f86d1e3");
	
	        // 查询版本对应修改记录
	//        findLogByVersion(projectId, "2.2");
	
	
	        // 创建公开项目
	//        String localString = "C:\\Users\\10242\\Desktop\\xmu";
	//        createPublicProject("lesley", "lesley1234", "xmu", "test create public project", localString);
	
	        // 获取公开项目
//	        getPublicProjects("lesley", "lesley1234");
	
	//        getProjects("lesley", "lesley1234");
	
	        // 删除远程库的项目及其相关的提交记录
	//        deletePorject("lesley", "lesley1234", "73");
	
	
	    }

	@Test
    void testMerge() throws IOException, GitAPIException, InterruptedException, URISyntaxException {
		
		
        String projectName = "new_project";
        String localString = "C:\\Users\\10242\\Desktop\\newp";

        // 项目发起者创建项目。
        // 同时，初始化的本地仓库默认在 master 分支下。
//        createProject("lyt", "lyt12345", projectName, localString);

        // 项目发起者查看所参与项目的信息
//        getProjects("lyt", "lyt12345");

        String projectId = "76";
        String remoteString = "http://101.43.149.150/lyt/new_project.git";

//        // 再根据 projectId 去查看项目的所有版本号
//        findAllVersions(projectId);   // 1.0
        
        
        
        

        
        
        
        // 项目发起者在本地 master 分支进行工作，再提交到远程库。
//      String message = "add bank.ecore lyt";
//      String branchName = "master";
//      MyGit.commitAndPush(localString, message, branchName);

      // 检查版本号是否自增
//      findAllVersions(projectId);   // 1.0 和 1.1
        
        
        
        
        
        
        
        
        // 添加成员
//        addMemmber("lyt", "lyt12345", "lesley", projectId, "30");
//        addMemmber("lyt", "lyt12345", "alice", projectId, "30");
//        addMemmber("lyt", "lyt12345", "test", projectId, "30");
		
		
        
        
        
        
        // 第一个成员进行了浅克隆
        String local_lesley = "C:\\Users\\10242\\Desktop\\newp_lesley";
//        String branchName = "master";   // 选择浅克隆的分支
//        MyGit.shallowCloneDepth1(local_lesley, branchName, remoteString);

        // 第二个成员也进行了浅克隆
        String local_alice = "C:\\Users\\10242\\Desktop\\newp_alice";
//        String branchName = "master";   // 选择浅克隆的分支
//        MyGit.shallowCloneDepth1(local_alice, branchName, remoteString);
        
        // 第三个成员也进行了浅克隆
        String local_test = "C:\\Users\\10242\\Desktop\\newp_test";
//        String branchName = "master";   // 选择浅克隆的分支
//        MyGit.shallowCloneDepth1(local_test, branchName, remoteString);
        
        
        
        
        
        
        
        
        
        
        
        
        

        // 第一个成员提交到新建的分支
//      createBranch(local_lesley, "branch_lesley");
        
//        String message = "modify bank.ecore lesley";
//        String branchName = "branch_lesley";
//        MyGit.commitAndPush(local_lesley, message, branchName);
        


        
        // 第二个成员提交到新建的分支
//      createBranch(local_alice, "branch_alice");
        
//      String message = "modify bank.ecore alice";
//      String branchName = "branch_alice";
//      MyGit.commitAndPush(local_alice, message, branchName);
        
        
        
        
        
        // 第三个成员提交到新建的分支
//      createBranch(local_test, "branch_test");
        
//      String message = "modify bank.ecore test";
//      String branchName = "branch_test";
//      MyGit.commitAndPush(local_test, message, branchName);
        
        

        
        
        
        
        // 需要服务器根据所选commit，得到 baseCommit
        String commit1 = "a9c890f05866100c6c070a27b7957485e38537dc";	// branch_lesley
        String commit2 = "c66b871b182ac8f1efab286154df8844794c66fe";	// branch_alice
        String commit3 = "40b56f00180688d82837c38cab566a8c64436899";	// branch_test
                
//        JSONArray arr = new JSONArray();
//        arr.add(commit1);
//        arr.add(commit2);
//        arr.add(commit3);
//        findParentBySHA("lesley", "lesley1234", projectId, arr.toString());
        
        String baseCommit = "d19a279012c6b5616c7daacbbc96cba2973e20dd";
        
        
        
        
        
        
        
        
        
        // 比如 lesley 想合并另外两个版本。
                                                                                 
        // 根据指定的 commitSHA，获取版本数据
        String fetch_base = "C:\\Users\\10242\\Desktop\\fetch_base";
//        MyGit.fetchBySHA(fetch_base, remoteString, baseCommit);
        
        // 根据指定的 commitSHA，获取版本数据
        String fetch_alice = "C:\\Users\\10242\\Desktop\\fetch_alice";
//        MyGit.fetchBySHA(fetch_alice, remoteString, commit2);
        
        // 根据指定的 commitSHA，获取版本数据
        String fetch_test = "C:\\Users\\10242\\Desktop\\fetch_test";
//        MyGit.fetchBySHA(fetch_test, remoteString, commit3);
        


        // 遍历父版本中的模型文件，若在两个分支版本中都存在，则调用多向模型合并，最后覆盖
//        List<String> pathList = new ArrayList<>();
//        pathList.add(fetch_base);
//        pathList.add(local_lesley);		// head
//        pathList.add(fetch_alice);
//        pathList.add(fetch_test);
//        
//        Set<String> extensions = new HashSet<>();
//        extensions.add("ecore");
//        extensions.add("uml");
//        extensions.add("xmi");
//        
        String tmpPath = "C:\\Users\\10242\\Desktop\\target";
//        findAndMergeModelFiles(pathList, extensions, tmpPath);
        
        
        
        // cherry pick 在模型文件合并后执行
        // cherry pick 另一个远程分支的某个版本
//      MyGit.cherryPick(local_lesley, commit2);

      // cherry pick 另一个远程分支的某个版本
//      MyGit.cherryPick(local_lesley, commit3);
        
        
        
        
        
                             
        // 合并后的模型文件，覆盖head中的模型文件。
        // 注意读取生成的辅助文件res.txt，默认保存在了tmpPath
//        replaceFiles(tmpPath, local_lesley);
                   
        // 再提交到远程库。
//      String message = "lesley: merge bank.ecore with commitSHA a9c890, c66b87 and 40b56f";
//      String branchName = "branch_lesley";
//      MyGit.commitAndPush(local_lesley, message, branchName);

        
        
        
        
    }
	
	@Test
	void testFindParent() throws IOException {
      String projectId = "76";
      
//      findAllVersions(projectId);
      
//      String version1 = "1.2";	// branch_lesley
//      String version2 = "1.3";	// branch_alice
//      String version3 = "1.4";	// branch_test
              
//      JSONArray arr = new JSONArray();
//      arr.add(version1);
//      arr.add(version2);
//      arr.add(version3);
      
//      findParent("lesley", "lesley1234", projectId, arr.toString());
      
      findAllVersions(projectId);
      
	}
	
	
	@Test
	void testCherryPick() throws IOException, InterruptedException, GitAPIException, URISyntaxException {
		
//        String remoteString = "http://101.43.149.150/lyt/new_project.git";
//        String projectId = "76";
		
		// 浅克隆后,在当前分支cherry-pick另一个分支的某个版本
//        String local_testCherry = "C:\\Users\\10242\\Desktop\\testCherry";
//        String branchName = "branch-lyt";   // 选择浅克隆的分支
//        MyGit.shallowCloneDepth1(local_testCherry, branchName, remoteString);
		
        
        // cherry pick 另一个远程分支的某个版本
//        MyGit.cherryPick(local_testCherry, "8869dd70294f5503070429f39705cf50f085989f");
        
        // cherry pick 再另外一个远程分支的某个版本
//        MyGit.cherryPick(local_testCherry,  "347713ddfa1728429ebe56d191bc63c486838525");
        
	}
	
	
	@Test
	void testPublicProject() throws IOException, GitAPIException, URISyntaxException {
		
//        String localString = "C:\\Users\\10242\\Desktop\\public_project1";
//        createPublicProject("alice", "alice1234", "public_project1", "test", localString);
                
//        getPublicProjects("alice", "alice1234");
	}
	
	/**
	 * 根据版本号获取父commit
	 * @param username
	 * @param password
	 * @param projectId
	 * @param jsonList
	 * @throws IOException
	 */
	public void findParent(String username, String password, String projectId, String jsonList) throws IOException {
		
		// 把双引号"转换为%22，记得要赋值！
		jsonList = jsonList.replaceAll("\"", "%22");
		
        String token = signIn(username, password);
        String res = gitGet("project/findParent" + "?projectId=" + projectId + "&jsonList=" + jsonList + "&token=" + token, 200);           
       
        System.out.println(res);
	}
	
	/**
	 * 根据多个commit获取父commit
	 * @param username
	 * @param password
	 * @param projectId
	 * @param jsonList
	 * @throws IOException
	 */
	public void findParentBySHA(String username, String password, String projectId, String jsonList) throws IOException {
		
		// 把双引号"转换为%22，记得要赋值！
		jsonList = jsonList.replaceAll("\"", "%22");
		
        String token = signIn(username, password);
        String res = gitGet("project/findParentBySHA" + "?projectId=" + projectId + "&jsonList=" + jsonList + "&token=" + token, 200);           
       
        System.out.println(res);
	}


	public void replaceFiles(String tmpPath, String targetPath) {
		
		BufferedReader reader;
		
		try {
			String resPath = tmpPath + "/" + "res.txt";
            reader = new BufferedReader(new FileReader(resPath));
            String line = reader.readLine();
            
            while(line != null) {
            	File from = new File(tmpPath + "/" + line);
                File to = new File(targetPath + "/" + line);
//                Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved successfully.");
                
                // read next line
                line = reader.readLine();
            }
            
            reader.close();
            
		} catch (IOException e) {
            e.printStackTrace();
		}
		
	}

	public void findAndMergeModelFiles(List<String> pathList, Set<String> extensions, String tmpPath) {
    	
    	int size = pathList.size();
    	TreeMap<String, List<File>> treeMap = new TreeMap<>();
    	List<String> res = new ArrayList<>();
    	
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		
		// 冲突
		ResourceSet resourceSet2 = new ResourceSetImpl();
		resourceSet2.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet2.getPackageRegistry().put(MyconflictPackage.eNS_URI, MyconflictPackage.eINSTANCE);
		
		// 指定需要进行排序的类型
		Set<String> needOrderSet = new HashSet<>();
		needOrderSet.add("eClassifiers");
		needOrderSet.add("eParameters");
    	
		// never use identifiers
		IMatchEngine.Factory.Registry registry = MatchEngineFactoryRegistryImpl.createStandaloneInstance();
		final MatchEngineFactoryImpl matchEngineFactory = new MatchEngineFactoryImpl(UseIdentifiers.NEVER);
		matchEngineFactory.setRanking(20);
		registry.add(matchEngineFactory);
		
		// 只使用一个EMFCompare
		EMFCompare build = EMFCompare.builder().setMatchEngineFactoryRegistry(registry).build();
    	    	
		// 根据基础版本的模型文件，去找分支版本的
    	for(int i=0; i<size; i++) {
        	File file = new File(pathList.get(i));           	                	
        	dfs(extensions, size, treeMap, i, file, new StringBuilder());
    	}
    	

    	for(Entry<String, List<File>> entry : treeMap.entrySet()) {
    		
    		String key = entry.getKey();
    		List<File> list = entry.getValue();

    		if(list.get(0) != null) {
    			// 核查是否在所有分支中都存在
    			boolean flag= list.stream().anyMatch(s -> s==null);
    			
    			if(flag == false) {
    				
    				res.add(key);
    				
    				List<org.eclipse.emf.common.util.URI> uriList = new ArrayList<>(size);
    				
    				for(int i=0; i<list.size(); i++) {
        				uriList.add(org.eclipse.emf.common.util.URI
        						.createFileURI(list.get(i).getAbsolutePath()));
    				}
    				
    				List<Resource> resourceList = new ArrayList<>(size);
    				Map<Resource, Integer> resourceMap = new HashMap<>();
    				for (int i = 0; i < size; i++) {
    					Resource resource = resourceSet.getResource(uriList.get(i), true);
    					resourceList.add(resource);
    					resourceMap.put(resource, i - 1); // 为了方便记录新加元素属于哪个分支模型，下表为0时，对应分支1
    				}
    				
    				
    				org.eclipse.emf.common.util.URI m1URI = org.eclipse.emf.common.util.URI
    						.createFileURI(tmpPath+ "/" + key);

    				
    				String path = FilenameUtils.getPath(key);
    				String baseName = FilenameUtils.getBaseName(key);
    				String extension = FilenameUtils.getExtension(key);
    				
    				org.eclipse.emf.common.util.URI conflictURI = org.eclipse.emf.common.util.URI
    						.createFileURI(tmpPath+ "/" + path + baseName + "_c." +extension);
    				
    				Resource m1Resource = resourceSet.createResource(m1URI); // 用createResource
    				Resource conflictResource = resourceSet2.createResource(conflictURI);    		
    				
    				NWay.nWay(resourceList, resourceMap, build, needOrderSet, m1Resource, conflictResource);
    			}
    		}
    		
    	}
    	
    	Path output = Paths.get(tmpPath+"/"+"res.txt");
    	
    	try {
			Files.write(output, res);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	}

    /**
     * 从.git层次开始，
     * 按照目录/文件名，将模型文件分组。
     * @param extensions
     * @param size
     * @param treeMap
     * @param i
     * @param file
     * @param sb
     */
	private void dfs(Set<String> extensions, int size, TreeMap<String, List<File>> treeMap, int i,
			File file, StringBuilder sb) {
		
		File[] fs = file.listFiles();
		
		for(File f : fs) {
			
			// 如果是目录，从.git层次开始
			if(f.getName().equals(".git")) {
				continue;
			} else if(f.isDirectory()) {
				sb.append(f.getName()+"/");
				
				dfs(extensions, size, treeMap, i, f, sb);
				
			} else {
				
				sb.append(f.getName());
				
				String key = sb.toString();
				String ex = FilenameUtils.getExtension(key);
				
				if(extensions.contains(ex)) {
					
					List<File> list = treeMap.get(key);
					if(list == null) {
						list = new ArrayList<>(Collections.nCopies(size, null));
						treeMap.put(key, list);
					}
					list.set(i, f);
					sb = new StringBuilder();
					
				}
			}
			
		}
	}
    
    
    
    

	/**
     *
     * @param localString
     * @param headCommit
     * @param srcCommit
     * @return
     * @throws IncorrectObjectTypeException
     * @throws IOException
     * @throws GitAPIException
     */
    public String getBaseCommit(String localString, String headCommit, String srcCommit)
            throws IncorrectObjectTypeException, IOException, GitAPIException {

        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        RevCommit a = null;
        RevCommit b = null;
        Iterable<RevCommit> logs = git.log().call();
        for(RevCommit rev : logs){
            if(rev.getId().getName().equals(headCommit)){
                a = rev;
            } else if(rev.getId().getName().equals(srcCommit)){
                b = rev;
            }
        }

        Repository repo = git.getRepository();
        RevWalk walk = new RevWalk(repo);

        walk.reset();
        walk.setRevFilter(RevFilter.MERGE_BASE);
        walk.markStart(a);
        walk.markStart(b);
        final RevCommit base = walk.next();
        if (base == null)
            return null;
        final RevCommit base2 = walk.next();
        if (base2 != null) {
            throw new NoMergeBaseException(
                    NoMergeBaseException.MergeBaseFailureReason.MULTIPLE_MERGE_BASES_NOT_SUPPORTED,
                    MessageFormat.format(
                            JGitText.get().multipleMergeBasesFor, a.name(), b.name(),
                            base.name(), base2.name()));
        }

        String baseCommit = base.getId().getName();
        return baseCommit;
    }

    /**
     * JGit 的 Merge
     * @param localString
     * @throws IOException
     * @throws GitAPIException
     */
    public void gitMerge(String localString) throws IOException, GitAPIException {
        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        Repository repository = git.getRepository();
        ObjectId fix = repository.resolve("fix");

        MergeResult mergeResult = git.merge().include(fix).setFastForward(MergeCommand.FastForwardMode.NO_FF).call();

        System.out.println(mergeResult);

        for (Map.Entry<String,int[][]> entry : mergeResult.getConflicts().entrySet()) {
            System.out.println("Key: " + entry.getKey());
            for(int[] arr : entry.getValue()) {
                System.out.println("value: " + Arrays.toString(arr));
            }
        }

    }

    public void gitLog(String localString) throws IOException, GitAPIException {
        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        Iterable<RevCommit> logs = git.log().call();
        for(RevCommit rev : logs){
            System.out.println(rev + ", message: " + rev.getShortMessage());
        }

    }


    /**
     * 获取最后的commitSHA
     * @param localString
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    public String getLatestCommit(String localString) throws IOException, GitAPIException {
        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        RevCommit latestCommit = git.log().setMaxCount(1).call().iterator().next();

        return latestCommit.getId().getName();
    }

    /**
     * 删除目录
     *
     * @param dir
     */
    public void deleteDirectory(String dir) {
        File file = new File(dir);
        try {
            FileUtils.deleteDirectory(file);
            System.out.println("Directory is deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 根据远程库地址获取分支信息
     *
     * @param remoteString
     * @throws GitAPIException
     */
    public void branchListRemote(String username, String password, String remoteString) throws GitAPIException {
        Collection<Ref> refs = Git.lsRemoteRepository()
                .setHeads(true)
                .setTags(true)
                .setRemote(remoteString)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call();

        System.out.println("\n************************All remote branches: ");
        for (Ref ref : refs) {
            System.out.println(ref);
        }
    }

    /**
     * 删除分支
     *
     * @param localString
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     */
    public void deleteLocalBranch(String localString, String branchName) throws IOException, GitAPIException {

        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        List<String> call = git.branchDelete().setBranchNames(branchName).setForce(true).call();
        System.out.println(call);

        System.out.println("\n" + call + " is deleted. ");
    }


    /**
     * 克隆到本地库后，从远程分支检出本地分支，称为跟踪分支。
     *
     * @param localString
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     */
    public void createTrackingBranch(String localString, String branchName) throws IOException, GitAPIException {
        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        Ref ref = git.checkout().
                setCreateBranch(true).
                setName(branchName).
                setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                setStartPoint("origin/" + branchName).
                call();

        System.out.println("Tracking Branch: " + branchName);
    }

    /**
     * 克隆远程库到本地后，查看所有分支信息，包括远程分支。
     *
     * @param localString
     * @throws IOException
     * @throws GitAPIException
     */
    public void branchList(String localString) throws IOException, GitAPIException {

        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        List<Ref> call = git.branchList().call();
        System.out.println("\n\n***************************All branches:");
        call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        for (Ref ref : call) {
            System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
        }
    }

    /**
     * 查询版本对应的修改记录
     * 注意,是黑板上的信息
     *
     * @param projectId
     * @param version
     */
    public void findLogByVersion(String projectId, String version) {
        String res = gitGet("blackboard/findLogByVersion" + "?projectId=" + projectId + "&version=" + version, 200);
        System.out.println("\n\n" + res);
    }


    /**
     * 绑定修改记录
     *
     * @param projectId
     * @param logId
     * @param commitSHA
     */
    public void bindLog(String projectId, String logId, String commitSHA) {
        String res = gitPost("blackboard/bindLog", new HashMap<String, String>() {{
            put("projectId", projectId);
            put("logId", logId);
            put("commitSHA", commitSHA);
        }}, 200);
        System.out.println(res);
    }


    /**
     * 查询项目的最新版本
     *
     * @param projectId
     */
    public void findLastedVersion(String projectId) {
        String res = gitGet("blackboard/findLastedVersion" + "?projectId=" + projectId, 200);
        System.out.println("\n\n" + res);
    }


    /**
     * 查询项目内的全部版本
     *
     * @param projectId
     */
    public void findAllVersions(String projectId) {
        String res = gitGet("blackboard/findAllVersion" + "?projectId=" + projectId, 200);
        System.out.println("\n\n" + res);
    }


    /**
     * 根据 commitSHA 查询版本号
     *
     * @param projectId
     * @param commitSHA
     */
    public void findVersionBySHA(String projectId, String commitSHA) {
        String res = gitGet("blackboard/findBySHA" + "?projectId=" + projectId + "&commitSHA=" + commitSHA, 200);
        System.out.println("\n\n" + res);
    }

    /**
     * 根据版本号查询提交
     *
     * @param projectId
     * @param version
     */
    public void findByVersion(String projectId, String version) {
        String res = gitGet("blackboard/findByVer" + "?projectId=" + projectId + "&version=" + version, 200);
        System.out.println("\n\n" + res);
    }

    /**
     * 删除远程库的项目
     * @param username
     * @param password
     * @param projectId
     * @throws IOException
     */
    public void deletePorject(String username, String password, String projectId) throws IOException {
        String token = signIn(username, password);
        String res = gitGet("project/delete" + "?projectId=" + projectId + "&token=" + token, 200);
        System.out.println("\n\n" + res);
    }

    /**
     * 获取公开项目
     * @param username
     * @param password
     * @throws IOException
     */
    public void getPublicProjects(String username, String password) throws IOException {
        String token = signIn(username, password);
        String res = gitGet("project/getAllProject" + "?token=" + token, 200);
        System.out.println("\n\n" + res);
    }

    /**
     * 根据模板 fork 项目
     *
     * @param username
     * @param password
     * @param projectId
     * @param name
     * @throws IOException
     */
    public void forkProject(String username, String password, String projectId, String name) throws IOException {
        String token = signIn(username, password);
        String res = gitPost("project/fork", new HashMap<String, String>() {{
            put("projectId", projectId);
            put("token", token);
            put("name", name);
        }}, 200);

        System.out.println(res);
    }


    /**
     * 检查是否有更新
     *
     * @param username
     * @param password
     * @param projectId
     * @param localString
     * @throws IOException
     */
    public void checkUpdate(String localString, String username, String password, String projectId, String branchName) throws IOException {
        String token = signIn(username, password);

        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());
        Repository repository = git.getRepository();

        ObjectId lastCommitId = repository.resolve(Constants.HEAD);

        RevWalk revWalk = new RevWalk(repository);
        RevCommit commit = revWalk.parseCommit(lastCommitId);
        int time = commit.getCommitTime();
        System.out.println("last CommitTime: " + time);

        String res = gitGet("project/checkUpdate" + "?time=" + time + "&id=" + projectId +"&name"+ branchName + "&token=" + token, 200);
        System.out.println("\n\n" + res);
    }

    public void realTime(String username, String password, String projectId) throws IOException, URISyntaxException {
        String token = signIn(username, password);
        MyWebSocket c = new MyWebSocket(new URI(
                "ws://101.43.149.150:8080/websocket/" + token + "/" + projectId));
        c.connect();
    }

    /**
     * 查看指定时间后的 Log
     *
     * @param username
     * @param password
     * @param projectId
     * @param date
     * @throws IOException
     */
    public void findLogByDate(String username, String password, String projectId, String date) throws IOException {
        String token = signIn(username, password);
        // date中的空格要转成%20！！！
        date = date.replaceAll(" ", "%20");
        String res = gitGet("blackboard/find" + "?token=" + token + "&date=" + date + "&projectId=" + projectId, 200);
        System.out.println("\n\n" + res);
    }

    /**
     * 查看所有 Log
     *
     * @param username
     * @param password
     * @param projectId
     * @throws IOException
     */
    public void findAllLogs(String username, String password, String projectId) throws IOException {
        String token = signIn(username, password);
        String res = gitGet("blackboard/findAll" + "?token=" + token + "&projectId=" + projectId, 200);
        System.out.println("\n\n" + res);
    }

    /**
     * 插入 Log 到黑板
     *
     * @param username
     * @param password
     * @param projectId
     * @param message
     * @throws IOException
     */
    public void insertLog(String username, String password, String projectId, String message) throws IOException {
        String token = signIn(username, password);
        String res = gitPost("blackboard/insert", new HashMap<String, String>() {{
            put("token", token);
            put("projectId", projectId);
            put("message", message);
        }}, 200);
        System.out.println("\n\n" + res);

        System.out.println("\n\n" + "insert is done");
    }

    /**
     * pull操作
     *
     * @param username
     * @param password
     * @param localString
     * @throws IOException
     * @throws GitAPIException
     */
    public void pullRepo(String username, String password, String localString) throws IOException, GitAPIException {
        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        PullCommand pullCommand = git.pull();
        pullCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
        PullResult result = pullCommand.call();
        FetchResult fetchResult = result.getFetchResult();
        for (TrackingRefUpdate update : fetchResult.getTrackingRefUpdates()) {
            System.out.println("\n\nlocalName: " + update.getLocalName());
            System.out.println("remoteName: " + update.getRemoteName());
            System.out.println("result: " + update.getResult());
        }
        System.out.println("\npull is done");

    }

    /**
     * 覆盖操作
     *
     * @param localString
     * @param toBranchName
     * @throws IOException
     * @throws GitAPIException
     */
    public void cover(String localString, String toBranchName) throws IOException, GitAPIException {
        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        // checkout to specific branch
        System.out.println("before checkout: " + git.getRepository().getFullBranch());
        git.checkout().setName(toBranchName).call();
        System.out.println("after checkout: " + git.getRepository().getFullBranch());

        git.reset().setMode(ResetCommand.ResetType.HARD).setRef("refs/heads/master").call();
        System.out.println("\n\ncover is done");

    }

    /**
     * 下载所有分支的数据
     *
     * @param username
     * @param password
     * @param projectId
     * @param downloadPath
     * @throws IOException
     */
    public void download(String username, String password, String projectId, String downloadPath) throws IOException {
        String token = signIn(username, password);
        MultipartFile allArchive = getAllArchive(projectId, token);
        File file = new File(downloadPath);
        allArchive.transferTo(file);
        System.out.println("\n\ndownload");
    }

    public MultipartFile getAllArchive(String projectId, String token) throws IOException {
        MultipartFile file = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl + "project/getAllArchive" + "?id=" + projectId + "&token=" + token);
        httpGet.addHeader("PRIVATE-TOKEN", token);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String filename = response.getFirstHeader("Content-Disposition").toString().split("\"")[1];
                HttpEntity httpEntity = response.getEntity();
                file = new MockMultipartFile(filename, httpEntity.getContent());
            }
            //关闭服务器响应
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 克隆仓库所有数据
     *
     * @param username
     * @param password
     * @param remoteString
     * @param localString
     * @throws GitAPIException
     * @throws IOException
     */
    public void cloneAll(String username, String password, String remoteString, String localString) throws GitAPIException, IOException {
        Path path = Paths.get(localString);
        CloneCommand cloneCommand = Git.cloneRepository();

        cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));

        try {
            Git git = cloneCommand.setURI(remoteString)
                    .setDirectory(path.toFile()).call();

            System.out.println("\n\nclone done");
        } catch (InvalidRemoteException e) {
            System.out.println("\n\nerror: 非项目成员，无法cloneAll");
            FileUtils.deleteDirectory(new File(localString));
        }

    }

    /**
     * 为项目添加成员
     *
     * @param username
     * @param password
     * @param newMemberName
     * @param projectId
     * @param accessLevel
     * @throws IOException
     */
    public void addMemmber(String username, String password, final String newMemberName, final String projectId, final String accessLevel) throws IOException {

        final String token = signIn(username, password);
        // {{url}}/project/addMember?projectId=2&username=root&accessLevel=20&token={{token}}
        String res = gitPost("project/addMember", new HashMap<String, String>() {{
            put("projectId", projectId);
            put("username", newMemberName);
            put("accessLevel", accessLevel);
            put("token", token);
        }}, 200);
        System.out.println("\n\n" + res);
    }

    /**
     * 获取用户所参与的项目
     *
     * @param username
     * @param password
     * @throws IOException
     */
    public void getProjects(String username, String password) throws IOException {
        String token = signIn(username, password);
        String res = gitGet("project/getProjects" + "?token=" + token, 200);
        JSONObject resultJson = JSONObject.parseObject(res);
        Result result = JSON.toJavaObject(resultJson, Result.class);
        System.out.println("\n\nResult Object: " + result);
    }

    /**
     * 切换分支
     *
     * @param localString
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     */
    public void checkOut(String localString, String branchName) throws IOException, GitAPIException {
        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        List<Ref> call = git.branchList().call();
        System.out.println("\n\nAll branches:");
        call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        for (Ref ref : call) {
            System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
        }

        // checkout to specific branch
        System.out.println("\nbefore checkout: " + git.getRepository().getFullBranch());
        git.checkout().setName(branchName).call();
        System.out.println("after checkout: " + git.getRepository().getFullBranch());
    }

    /**
     * 提交到远程库
     *
     * @param username
     * @param password
     * @param localString
     * @param message
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     * @throws URISyntaxException
     */
    public void commitAndPush(String username, String password, String localString, String message, String branchName) throws IOException, GitAPIException, URISyntaxException {

        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        // git add
        git.add().addFilepattern(".").call();

        // git commit
        RevCommit revCommit = git.commit()
                .setMessage(message)
                .call();
        ObjectId commitId = revCommit.getId();
        System.out.println("\n\ncommit done");

        // git push
        Iterable<PushResult> results = git.push()
                .setRefSpecs(new RefSpec(branchName + ":" + branchName))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call();

        PushResult next = results.iterator().next();
        Collection<RemoteRefUpdate> remoteUpdates = next.getRemoteUpdates();

        for (RemoteRefUpdate r : remoteUpdates) {
            System.out.println(r);
        }

    }

    /**
     * 版本号自增
     *
     * @param projectId
     * @param commitSHA
     */
    public void addVersion(String projectId, String commitSHA) {
        String res = gitPost("blackboard/addVersion", new HashMap<String, String>() {{
            put("projectId", projectId);
            put("commitSHA", commitSHA);
        }}, 200);
        System.out.println(res);
    }


    /**
     * 创建公开项目
     * @param username
     * @param password
     * @param projectName
     * @param description
     * @throws IOException
     */
    public void createPublicProject(String username, String password, String projectName, String description, String localString) throws IOException, GitAPIException, URISyntaxException {
        Path path = Paths.get(localString);
        String token = signIn(username, password);


        String res = gitPost("project/createPublicProject", new HashMap<String, String>() {{
            put("name", projectName);
            put("token", token);
            put("description", description);
        }}, 200);

        System.out.println(res);

        // initialize local repository
        InitCommand init = Git.init();
        init.setDirectory(path.toFile());
        Git git = init.call();
        Repository repository = git.getRepository();
        System.out.println("\n\ninitialize local repository: " + repository.getDirectory());

        // push README.md to remote master
        // or ref head not found error
        Files.writeString(path.resolve("README.md"), "Hello :)");
        git.add().addFilepattern("README.md").call();
        RevCommit revCommit = git.commit().setMessage("create README.md").call();
        System.out.println("\n\ncommit done");

        // 为本地库绑定远程库信息
        String remoteString = "http://101.43.149.150/" + username + "/" + projectName + ".git";
        RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish(remoteString));
        remoteAddCommand.call();

        //默认 push 到远程库的 master 分支
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
        Iterable<PushResult> results = pushCommand.call();

        PushResult next = results.iterator().next();
        Collection<RemoteRefUpdate> remoteUpdates = next.getRemoteUpdates();

        for (RemoteRefUpdate r : remoteUpdates) {
            System.out.println(r);
        }

    }

    /**
     * 创建远程项目，同时初始化本地库
     *
     * @param username
     * @param password
     * @param projectName
     * @param localString
     * @throws IOException
     * @throws GitAPIException
     * @throws URISyntaxException
     */
    public void createProject(String username, String password, String projectName, String localString) throws IOException, GitAPIException, URISyntaxException {

        Path path = Paths.get(localString);
        String token = signIn(username, password);

        // create remote project
        //完整url为http://localhost:8080/gitserver/project/create?name=test4&token={{token}}
        String res = gitPost("project/create", new HashMap<String, String>() {{
            put("name", projectName);
            put("token", token);
        }}, 200);

        JSONObject resultJson = JSONObject.parseObject(res);
        Result result = JSON.toJavaObject(resultJson, Result.class);
        System.out.println("\n\nResult Object: " + result);

        // initialize local repository
        InitCommand init = Git.init();
        init.setDirectory(path.toFile());
        Git git = init.call();
        Repository repository = git.getRepository();
        System.out.println("\n\ninitialize local repository: " + repository.getDirectory());

        // push README.md to remote master
        // or ref head not found error
        Files.writeString(path.resolve("README.md"), "Hello :)");
        git.add().addFilepattern("README.md").call();
        RevCommit revCommit = git.commit().setMessage("create README.md").call();
        System.out.println("\n\ncommit done");

        // 为本地库绑定远程库信息
        String remoteString = "http://101.43.149.150/" + username + "/" + projectName + ".git";
        RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish(remoteString));
        remoteAddCommand.call();

        //默认 push 到远程库的 master 分支
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
        Iterable<PushResult> results = pushCommand.call();

        PushResult next = results.iterator().next();
        Collection<RemoteRefUpdate> remoteUpdates = next.getRemoteUpdates();

        for (RemoteRefUpdate r : remoteUpdates) {
            System.out.println(r);
        }

    }

    /**
     * 创建本地分支，同时切换
     *
     * @param localString
     * @param branchName
     * @throws GitAPIException
     * @throws IOException
     */
    public void createBranch(String localString, String branchName) throws GitAPIException, IOException {

        Path path = Paths.get(localString);
        Git git = Git.open(path.toFile());

        // 创建本地分支
        git.branchCreate().setName(branchName).call();
        System.out.println("\n\n" + branchName + " is created");

        // checkout to a specific branch
        System.out.println("before checkout: " + git.getRepository().getFullBranch());
        git.checkout().setName(branchName).call();
        System.out.println("after checkout: " + git.getRepository().getFullBranch());
    }

    /**
     * 用户登录
     *
     * @param username: 用户名或邮箱
     * @param password: 密码
     * @return token
     * @throws IOException
     */
    public String signIn(String username, String password) throws IOException {
        //完整url为http://101.43.149.150:8080/gitserver/user/signIn?login=test&password=test1234
        String res1 = gitGet("user/signIn" + "?login=" + username + "&password=" + password, 200);

        JSONObject resultJson = JSONObject.parseObject(res1);
        Result result1 = JSON.toJavaObject(resultJson, Result.class);
        System.out.println("\n\nResult Object: " + result1);
        String token = result1.getData();

        return token;
    }

    /**
     * 用户注册
     *
     * @param name:     姓名
     * @param username: 用户名（不可重复）
     * @param email:    邮箱（不可重复）
     * @param password: 密码（大于8位）
     */
    public void signUp(final String name, final String username, final String email, final String password) {
        //完整url为http://101.43.149.150:8080/gitserver/user/signUp?name=test5&username=test5&email=test5@test.com&password=asdf1234
        String res = gitPost("user/signUp", new HashMap<String, String>() {{
            put("name", name);
            put("username", username);
            put("email", email);
            put("password", password);
        }}, 200);
        System.out.println("\n\n" + res);
    }

    public String gitGet(String url, int OkCode) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl + url);
        return getResponse(httpGet, client, OkCode);
    }

    public String gitPost(String url, Map<String, String> params, int OkCode) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl + url);
        List<NameValuePair> formList = new ArrayList<>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            formList.add(new BasicNameValuePair(key, params.get(key)));
        }
        StringEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formList, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(entity);

        return getResponse(httpPost, client, OkCode);
    }

    public String getResponse(HttpRequestBase httpRequest, CloseableHttpClient httpClient, int OkCode) {
        CloseableHttpResponse response;
        String res = null;

        try {
            response = httpClient.execute(httpRequest);
            //System.out.println(response.getStatusLine().toString());
            //System.out.println(response.getEntity().getContentType());
            //System.out.println(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() == OkCode) {
                //特别注意EntityUtils.toString会将CloseableHttpResponse的资源消耗掉
                //调用一次后会关闭其中的IOStream，后不能再接任何赋值
                //例如之前的println语句中调用了该函数，解注释后再运行这里会报错Stream closed
                res = EntityUtils.toString(response.getEntity());
            } else {
                res = response.getStatusLine().toString();
            }
            //关闭服务器响应
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}

