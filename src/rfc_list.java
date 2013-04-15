public interface rfc_list {

	class RFCLink {
    public String rfc_title;
    public String rfc_num;
    public String hostname_peer;
    public RFCLink nextLink;

    //Link constructor
    public RFCLink(String rfct,String rfcn,String hname) {
	    rfc_title =rfct;
    	hostname_peer = hname;
	    rfc_num = rfcn;
    }

    //Print Link data
    public void printLink() {
	    System.out.print("{" + rfc_title + ", " + rfc_num + ", " + hostname_peer + "}");
    }
}

class RFCList {
    private RFCLink first;

    //LinkList constructor
    public RFCList() {
	    first = null;
    }

    //Returns true if list is empty
    public boolean isEmpty() {
	    return first == null;
    }

    //Inserts a new Link at the first of the list
    public void insert(String rfc_title, String rfc_number, String hostname_peer) {
	    RFCLink link = new RFCLink(rfc_title, rfc_number,hostname_peer);
	    link.nextLink = first;
	    first = link;
    }

    //Deletes the link at the first of the list
    public RFCLink delete() {
	    RFCLink temp = first;
	    first = first.nextLink;
	    return temp;
    }

    //Prints list data
    public void printList() {
	    RFCLink currentLink = first;
	    System.out.print("List: ");
	    while(currentLink != null) {
		    currentLink.printLink();
		    currentLink = currentLink.nextLink;
	    }
	    System.out.println("");
    }
    
    public String lookUp(String rfcnum)
    {
    	RFCLink currentLink = first;
    	String rfclist = null;
    	while(currentLink != null)
    	{
    		if(currentLink.rfc_num.equals(rfcnum))
    			rfclist = rfclist.concat(currentLink.hostname_peer);
    			rfclist = rfclist.concat(" ");
    	}
    	return rfclist;
    }
}  
}