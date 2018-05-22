/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.ejb;

import dcbank.entity.Transferencia;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
}
