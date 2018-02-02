package de.hpi.sam.rubis.usermgmt.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.hpi.sam.rubis.entity.Region;
import de.hpi.sam.rubis.queryservice.BasicQueryService;
import de.hpi.sam.rubis.queryservice.QueryServiceException;
import de.hpi.sam.rubis.usermgmt.BrowseRegionsService;
import de.hpi.sam.rubis.usermgmt.BrowseRegionsServiceException;

/**
 * Implementation of the {@link BrowseRegionsService}.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = BrowseRegionsService.MAPPED_NAME)
public class BrowseRegionsServiceBean implements BrowseRegionsService {

	@EJB(mappedName = BasicQueryService.MAPPED_NAME)
	private BasicQueryService basicQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Region> getAllRegions() throws BrowseRegionsServiceException {

		try {
			List<Region> regions = this.basicQueryService.findAllRegions();
			return regions;
		} catch (QueryServiceException e) {
			throw new BrowseRegionsServiceException(
					"Error in retrieving all regions.", e);
		}

	}

}
