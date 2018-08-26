package com.middleLayer;

import java.io.File;
import java.util.UUID;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2008_06.DataManagement.AttrInfo;
import com.teamcenter.services.strong.core._2008_06.DataManagement.GetItemAndRelatedObjectsInfo;
import com.teamcenter.services.strong.core._2008_06.DataManagement.GetItemAndRelatedObjectsItemOutput;
import com.teamcenter.services.strong.core._2008_06.DataManagement.GetItemAndRelatedObjectsResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.RevisionOutput;
import com.teamcenter.soa.client.FileManagementUtility;
import com.teamcenter.soa.client.GetFileResponse;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.Dataset;

public class GetFileFromTeamcenter {
	
	private FileManagementUtility fileManager;
	private File file;
	
	public GetFileFromTeamcenter(String id, String fileName, String revisionId) 
	{
		try 
		{
			file = getInstructionsFromTeamcenter(id, fileName, revisionId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public File getFile() 
	{
		return file;
	}
	
	private File getInstructionsFromTeamcenter(String id, String fileName, String revisionId) throws Exception 
	{
		UUID clientId = UUID.randomUUID();
		DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());

		/** Prepare the search criteria */
		GetItemAndRelatedObjectsInfo[] infos = new GetItemAndRelatedObjectsInfo[1];
		infos[0] = new GetItemAndRelatedObjectsInfo();

		infos[0].itemInfo.ids = new AttrInfo[]{new AttrInfo()};
		
		infos[0].clientId = clientId.toString();
		infos[0].datasetInfo.filter.name = fileName;
		infos[0].itemInfo.ids[0].name = "item_id";
		infos[0].itemInfo.ids[0].value = id;
		infos[0].itemInfo.useIdFirst = true;
		infos[0].datasetInfo.filter.processing = "All";
		
		/**********************/
		/** Call the services */
		/**********************/
		GetItemAndRelatedObjectsResponse response = dmService.getItemAndRelatedObjects(infos);
		
		if (response.output.length == 0)
		throw new Exception("No item with the ID " + id + " was found");
		else if (response.output.length > 1)
		throw new Exception("More than one item with the ID " + id + " was found");
		
		GetItemAndRelatedObjectsItemOutput itemOutput = response.output[0];
		
		/** Print some debug info */
		String name = itemOutput.item.get_object_name();
		
		
		System.out.println("Item name: " + name);
		System.out.println("Item object string: " + itemOutput.item.get_object_string());
		System.out.println("Item ID: " + itemOutput.item.get_item_id());
		System.out.println("Object type: " + itemOutput.item.get_object_type());
		System.out.println("Object string: " + itemOutput.item.get_object_string());
		
		//TODO
		//Bu kisimdaki Dataseti alabilmek icin oncelikle icindeki "revision" a ulasmak lazim. 
		//Bu revisiona ulasmak icin de Idleri split ettigimiz yerde "revID" olusturmali ve split edilen kismin kalanini 
		//parametre olarak gondermeliyiz
		
		/** Note: The revisions in the revision array are stored in descending order (the latest revision first) */
		RevisionOutput revOutput = null;
		
		/************************************************/
		/** Obtain the file through the revision number */
		/** */
		/** 001111/2016-04-11#17:06 */
		/************************************************/
		
		for( RevisionOutput revision : itemOutput.itemRevOutput )
		{
			// idParts[1] is "A", "B", "C".
			if( revisionId.equals( revision.itemRevision.get_item_revision_id() ))
			{
				revOutput = revision;
				break;
			}

			if( revOutput == null)
			{
				throw new RuntimeException("A revision with the ID "+ revisionId +" was not found for the item with ID "+ id);
			}
			else
			{
				revOutput = itemOutput.itemRevOutput[0];
			}
		}
		
		/**************************************************************************************/
		/** Print some debug info */
		System.out.println("Latest revision name: "
		+ revOutput.itemRevision.get_object_name());
		System.out.println("Latest revision object string: "
		+ revOutput.itemRevision.get_object_string());
		System.out.println("Latest revision item ID: "
		+ revOutput.itemRevision.get_item_revision_id());
		System.out.println("Latest revision item revision ID: "
		+ revOutput.itemRevision.get_item_revision_id());
		/** It is assumed that only one item corresponds with the ID and revision */
		if (revOutput.datasetOutput.length == 0)
		{
			return null;
		}
		else if(revOutput.datasetOutput.length > 1)
		{
			throw new Exception("The item revision has ambiguous data");
		}
		
		
		Dataset dataset = revOutput.datasetOutput[0].dataset;
		
		
		/** Print some debug info */
		System.out.println("Dataset: " + dataset.get_object_name());
		for (String ref : dataset.get_ref_names())
		{
			System.out.println("Dataset reference: " + ref);
		}
		
		/** Assume there is only one reference contained in the data set (this is the actual file) */
		ModelObject[] references = new ModelObject[] { dataset.get_ref_list()[0] };
		GetFileResponse fresp = this.fileManager.getFiles(references);
		
		/** Print some debug info */
		System.out.format("Downloaded %d of %d files with %d partial errors.\n", fresp.sizeOfFiles(), references.length, fresp.sizeOfPartialErrors());
		System.out.println("Amount of files "+fresp.getFiles().length);
		System.out.println("File "+fresp.getFile(0));
		System.out.println("Files as path "+fresp.getFile(0).toPath());
		
		return fresp.getFile(0);
	}

}
