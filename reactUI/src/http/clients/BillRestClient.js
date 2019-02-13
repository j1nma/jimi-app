import RestClient from "../RestClient";

export default class BillRestClient extends RestClient {


  /**
   *  Perform http request to get bills
   *
   *  @returns {Promise} - The http promise.
   * @param page
   * @param pagesize
   */
  get(page, pagesize) {
    return this.instance.get(
      'api/admin/bills'
    );
  }

  /**
   *
   * @param id
   * @return Promise
   */
  getBill(id) {
    return this.instance.get(
      'api/admin/bills/'+id
    );
  }
}
