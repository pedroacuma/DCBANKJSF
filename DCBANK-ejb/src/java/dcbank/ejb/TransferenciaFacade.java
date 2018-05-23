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
public class TransferenciaFacade extends AbstractFacade<Transferencia> {

    @PersistenceContext(unitName = "DCBANK-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransferenciaFacade() {
        super(Transferencia.class);
    }
    
    public List<Transferencia> buscarPorCuenta (Cuenta cuenta) {
        Query q = this.em.createQuery("select t from Transferencia t where t.cuenta = :cuenta");
        q.setParameter("cuenta", cuenta);
        
        List<Transferencia> listaMovimientos;
        try{
           listaMovimientos = (List<Transferencia>) q.getResultList();
        }catch(Exception ex){
           listaMovimientos = null;
        }
        
        return  listaMovimientos;        
    }
    
    public List<Transferencia> buscarPorConcepto (String concepto) {
        Query q = this.em.createNamedQuery("Transferencia.findByConcepto", Transferencia.class);
        q.setParameter("concepto", "%"+concepto+"%");
        
        List<Transferencia> listaT;
        try{
           listaT = (List<Transferencia>) q.getResultList();
        }catch(Exception ex){
           listaT = null;
        }
        
        return  listaT;        
    }
    
    public List<Transferencia> buscarPorBeneficiario (String beneficiario) {
        Query q = this.em.createNamedQuery("Transferencia.findByBeneficiario", Transferencia.class);
        q.setParameter("beneficiario", "%"+beneficiario+"%");
        
        List<Transferencia> listaT;
        try{
           listaT = (List<Transferencia>) q.getResultList();
        }catch(Exception ex){
           listaT = null;
        }
        
        return  listaT;        
    }
    
}
