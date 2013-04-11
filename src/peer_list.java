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

/*class peer_list {
    public static void main(String[] args) {
	    PeerList list = new PeerList();

	    list.insert("String1",10);
	    list.insert("String2",20);
	    list.insert("String3",30);
	    list.insert("String4",40);
	    list.insert("String5",50);

	    list.printList();

	    while(!list.isEmpty()) {
		    PeerLink deletedLink = list.delete();
		    System.out.print("deleted: ");
		    deletedLink.printLink();
		    System.out.println("");
	    }
	    list.printList();
    }
}*/
