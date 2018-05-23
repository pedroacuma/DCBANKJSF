/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.ejb;

import dcbank.entity.Cuenta;
import dcbank.entity.Transferencia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ofviak
 */
@Stateless
public class CuentaFacade extends AbstractFacade<Cuenta> {

    @PersistenceContext(unitName = "DCBANK-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CuentaFacade() {
        super(Cuenta.class);
    }

    public Cuenta findByIBAN(String iban) {
        Query q = this.em.createNamedQuery("Cuenta.findByIban");
        q.setParameter("iban", iban);
        Cuenta c = (Cuenta) q.getSingleResult();
        return c;
    }
    
    public Cuenta buscarPorIban (String iban) {
        Query q = this.em.createNamedQuery("Cuenta.findByIban", Cuenta.class);
        q.setParameter("iban", iban);
        
        Cuenta cuenta;
        try{
           cuenta = (Cuenta) q.getSingleResult();
        }catch(Exception ex){
           cuenta = null;
        }
        
        return  cuenta;        
    }


    
}
