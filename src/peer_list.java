public interface peer_list
{
	
class PeerLink {
    public String hostname;
    public String portnumber;
    public PeerLink nextLink;

    //Link constructor
    public PeerLink(String hname, String pn) {
	    hostname = hname;
	    portnumber = pn;
    }

    //Print Link data
    public void printLink() {
	    System.out.print("{" + hostname + ", " + portnumber + "} ");
    }
}

class PeerList {
    private PeerLink first;

    //LinkList constructor
    public PeerList() {
	    first = null;
    }

    //Returns true if list is empty
    public boolean isEmpty() {
	    return first == null;
    }

    //Inserts a new Link at the first of the list
    public void insert(String hostname, String portnumber) {
	    PeerLink link = new PeerLink(hostname, portnumber);
	    link.nextLink = first;
	    first = link;
    }

    //Deletes the link at the first of the list
    public PeerLink delete() {
	    PeerLink temp = first;
	    first = first.nextLink;
	    return temp;
    }

    //Prints list data
    public void printList() {
	    PeerLink currentLink = first;
	    System.out.print("List: ");
	    while(currentLink != null) {
		    currentLink.printLink();
		    currentLink = currentLink.nextLink;
	    }
	    System.out.println("");
    }
    
    public String lookUp(String hname)
    {
    	PeerLink currentLink = first;
    	String portnum = null;
    	while(currentLink != null)
    	{
    		if(currentLink.hostname.equals(hname))
    			portnum = currentLink.portnumber;
    	}
    	return portnum;
    }
}  
}