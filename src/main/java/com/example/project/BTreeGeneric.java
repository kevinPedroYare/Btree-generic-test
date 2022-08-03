package com.example.project;

public class BTreeGeneric<E extends Comparable<? super E>> {
    private BNode<E> root;
    private int orden;

    private boolean up;
	private BNode<E> nDes;

    public BTreeGeneric(int orden) {
		this.orden = orden;
		this.root = null;
	}

    public boolean add(E value) {
        up = false;
		E mediana;
		BNode<E> pnew;
		mediana = push(this.root, value);
		
		if(up) {
			pnew = new BNode<E>(this.orden);
			pnew.count = 1;
			pnew.keys.set(0, mediana);
			pnew.childs.set(0, this.root);
			pnew.childs.set(1, nDes);
			this.root = pnew;
		}
        return true;
    }

    private E push(BNode<E> current, E value) {
		int pos[] = new int[1];
		E mediana;
		if(current == null) {
			up = true;
			nDes = null;
			return value;
		}
		else {
			boolean fl;
			fl = searchNode(current, value, pos);
			if(fl) {
				System.out.print("Item duplicado\n");
				up = false;
				return null;
			}
			mediana = push(current.childs.get(pos[0]), value);
			if(up) {
				if(current.nodeFull(this.orden-1))
					mediana = dividedNode(current, mediana, pos[0]);
				else {
					up = false;
					putNode(current, mediana, nDes, pos[0]);
				}
			}
			return mediana;
		}
	}

	private void putNode(BNode<E> current, E value, BNode<E> rd, int k) {
		int i;
		for(i = current.count-1; i >= k; i--) {
			current.keys.set(i+1, current.keys.get(i));
			current.childs.set(i+2, current.childs.get(i+1));
		}
		current.keys.set(k, value);
		current.childs.set(k+1, rd);
	}
	
	private E dividedNode(BNode<E> current, E value, int k) {
		BNode<E> rd = nDes;
		int i, posMdna;
		posMdna = (k <= this.orden/2) ? this.orden/2 : this.orden/2+1;
		
		nDes = new BNode<E>(this.orden);
		for(i = posMdna; i < this.orden-1; i++) {
			nDes.keys.set(i-posMdna, current.keys.get(i));
			nDes.childs.set(i-posMdna+1, current.childs.get(i+1));
		}
		
		nDes.count = (this.orden- 1 ) - posMdna;
		current.count = posMdna;
		if(k <= this.orden/2)
			putNode(current, value, rd, k);
		else
			putNode(nDes, value, rd, k-posMdna);
		
		E median = current.keys.get(current.count-1);
		nDes.childs.set(0, current.childs.get(current.count));
		current.count--;
		return median;
	}

    public E remove(E value) {
        if (root == null){
            return null;
        }
		BNode<E> current = root;
		int pos[] = new int[1];
		boolean fl;
		fl = searchNode(current, value, pos);
		if(!fl) {
			System.out.print("Item no encontrado\n");
			
		}
		else {
			E mediana = remove(current, pos[0]);
			if(current.count == 0) {
				root = current.childs.get(0);
			}
			return mediana;
		}
		return null;
    }

	private E remove(BNode<E> current, int i) {
		E mediana;
		if(current.count == 1) {
			mediana = current.keys.get(0);
			current.keys.set(0, null);
			current.childs.set(0, null);
			current.count = 0;
		}
		else {
			BNode<E> rd = current.childs.get(i+1);
			if(rd.count > this.orden/2) {
				mediana = removeMax(rd, current, i);
			}
			else {
				mediana = removeMin(rd, current, i);
			}
		}
		return mediana;
	}

    private E removeMin(BNode<E> rd, BNode<E> current, int i) {
		E mediana;
		mediana = rd.keys.get(0);
		current.keys.set(i, mediana);
		rd.keys.set(0, null);
		rd.childs.set(0, rd.childs.get(1));
		rd.count--;
		return mediana;
	}

	private E removeMax(BNode<E> rd, BNode<E> current, int i) {
		E mediana;
		mediana = rd.keys.get(rd.count-1);
		current.keys.set(i, mediana);
		rd.keys.set(rd.count-1, null);
		rd.childs.set(rd.count, null);
		rd.count--;
		return mediana;
	}

	public void clear() {
		root = null;
    }

    public boolean search(E value) {
        return search(this.root, value);
    }

	private boolean search(BNode<E> current, E value) {
		int pos[] = new int[1];
		boolean fl;
		
		if(current == null)
			return false;
		else {
			fl = searchNode(current, value, pos);
			if(fl) {
				System.out.print("Item " + value + " encontrado en la posicion " + pos[0]);
				return true;
			}
			else
				return search(current.childs.get(pos[0]), value);
		}
	}

	private boolean searchNode(BNode<E> current, E value, int pos[]) {
		pos[0] = 0;
		while(pos[0] < current.count && current.keys.get(pos[0]).compareTo(value) < 0)
			pos[0]++;
		if(pos[0] == current.count)
			return false;
		return(value.equals(current.keys.get(pos[0])));
	}

    public int size() {
		return size(this.root);
    }

	private int size(BNode<E> root) {
		if(root == null)
			return 0;
		else {
			int i, sum = 0;
			for(i = 0; i < root.count; i++)
				sum += size(root.childs.get(i));
			return sum + 1;
		}
	}

	private String toString(BNode<E> root) {
		if(root == null)
			return "";
		else {
			String s = "";
			int i;
			for(i = 0; i < root.count; i++) {
				s += root.keys.get(i) + " ";
				s += toString(root.childs.get(i));
			}
			return s;
		}
	}
}
