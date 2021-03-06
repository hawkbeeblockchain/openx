/*
+---------------------------------------------------------------------------+
| OpenX v${RELEASE_MAJOR_MINOR}                                                                |
| ======${RELEASE_MAJOR_MINOR_DOUBLE_UNDERLINE}                                                                 |
|                                                                           |
| Copyright (c) 2003-2009 OpenX Limited                                     |
| For contact details, see: http://www.openx.org/                           |
|                                                                           |
| This program is free software; you can redistribute it and/or modify      |
| it under the terms of the GNU General Public License as published by      |
| the Free Software Foundation; either version 2 of the License, or         |
| (at your option) any later version.                                       |
|                                                                           |
| This program is distributed in the hope that it will be useful,           |
| but WITHOUT ANY WARRANTY; without even the implied warranty of            |
| MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             |
| GNU General Public License for more details.                              |
|                                                                           |
| You should have received a copy of the GNU General Public License         |
| along with this program; if not, write to the Free Software               |
| Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA |
+---------------------------------------------------------------------------+
$Id: TestAddZone.java 20704 2008-05-28 13:28:08Z pawel.dachterski@openx.org $
*/

package org.openx.zone;

import java.net.MalformedURLException;
import org.apache.xmlrpc.XmlRpcException;
import org.openx.utils.ErrorMessage;
import org.openx.utils.TextUtils;

/**
 * Verify Link Campaign method
 *
 * @author     Pawel Dachterski <pawel.dachterski@openx.org>
 */
public class TestZoneLinkCampaign extends ZoneTestCase {

	protected Integer zoneId = null;
	protected Integer campaignId = null;
	
	protected void setUp() throws Exception {
		super.setUp();

		campaignId = createCampaign();
		zoneId = createZone();
	}

	protected void tearDown() throws Exception {

		deleteCampaign(campaignId);
		deleteZone(zoneId);

		super.tearDown();
	}
	
	/**
	 * Execute test method with error
	 *
	 * @param params -
	 *            parameters for test method
	 * @param errorMsg -
	 *            true error messages
	 * @throws MalformedURLException
	 */
	private void executeLinkCampaignWithError(Object[] params, String errorMsg)
		throws MalformedURLException {
		
		try {
			execute(ZONE_LINK_CAMPAIGN_METHOD, params);
			fail(ErrorMessage.METHOD_EXECUTED_SUCCESSFULLY_BUT_SHOULD_NOT_HAVE);
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, errorMsg, e
					.getMessage());
		}
	}

	/**
	 * Test method with all required fields and some optional.
	 *
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testLinkCampaignAllReqAndSomeOptionalFields()
			throws XmlRpcException, MalformedURLException {
		
		Object[] XMLRPCMethodParameters = new Object[] { sessionId, zoneId, campaignId };
		final Boolean result = (Boolean) client
				.execute(ZONE_LINK_CAMPAIGN_METHOD, XMLRPCMethodParameters);
		
		assertTrue(result);
	}

	/**
	 * Test method for case when linking campaign once again with same id.
	 *
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testLinkCampaignOnceAgain()
			throws XmlRpcException, MalformedURLException {
		
		Object[] XMLRPCMethodParameters = new Object[] { sessionId, zoneId, campaignId };
		client.execute(ZONE_LINK_CAMPAIGN_METHOD, XMLRPCMethodParameters);
		
		//TODO: Add expected behavior from https://developer.openx.org/jira/browse/OX-3296
		final Boolean result = (Boolean) client
				.execute(ZONE_LINK_CAMPAIGN_METHOD, XMLRPCMethodParameters);
		
		assertTrue(result);
	}
	
	/**
	 * Test methods for Unknown ID Error, described in API
	 *
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testLinkCampaignUnknownZoneIdError() throws MalformedURLException,
			XmlRpcException {
		
		Integer zoneId = createZone();
		assertNotNull(zoneId);
		deleteZone(zoneId);

		Object[] XMLRPCMethodParameters = new Object[] { sessionId, zoneId, campaignId };

		executeLinkCampaignWithError(XMLRPCMethodParameters, ErrorMessage.getMessage(
				ErrorMessage.UNKNOWN_ID_ERROR, ZONE_ID));
	}

	/**
	 * Test methods for Unknown ID Error, described in API
	 *
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testLinkCampaignUnknownCampaignIdError() throws MalformedURLException,
			XmlRpcException {
		
		Integer campaignId = createCampaign();
		assertNotNull(campaignId);
		deleteCampaign(campaignId);

		Object[] XMLRPCMethodParameters = new Object[] { sessionId, zoneId, campaignId };

		executeLinkCampaignWithError(XMLRPCMethodParameters, ErrorMessage.getMessage(
				ErrorMessage.UNKNOWN_ID_ERROR, CAMPAIGN_ID));
	}
	
	/**
	 * Test method with fields that has value of wrong type (error).
	 *
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testLinkCampaignCampaignIdWrongTypeError() throws MalformedURLException,
			XmlRpcException {
		
		Object[] XMLRPCMethodParameters = new Object[] { sessionId, zoneId, TextUtils.NOT_INTEGER };

		executeLinkCampaignWithError(XMLRPCMethodParameters, ErrorMessage.getMessage(
				ErrorMessage.INCORRECT_PARAMETERS_WANTED_INT_GOT_STRING, "3"));
	}
	
	/**
	 * Test method with fields that has value of wrong type (error).
	 *
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testLinkCampaignZoneIdWrongTypeError() throws MalformedURLException,
			XmlRpcException {

		Object[] XMLRPCMethodParameters = new Object[] { sessionId, TextUtils.NOT_INTEGER, campaignId };

		executeLinkCampaignWithError(XMLRPCMethodParameters, ErrorMessage.getMessage(
				ErrorMessage.INCORRECT_PARAMETERS_WANTED_INT_GOT_STRING, "2"));
	}
	
	/**
	 * Test method with wrong sessionId.
	 *
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testLinkCampaignZoneWrongSessionIdError() throws MalformedURLException,
			XmlRpcException {

		String sessionId = "phpads11111111111111.11111111";
		Object[] XMLRPCMethodParameters = new Object[] { sessionId, zoneId, campaignId };

		executeLinkCampaignWithError(XMLRPCMethodParameters, ErrorMessage.getMessage(
				ErrorMessage.INVALID_SESSION_ID));
	}
}