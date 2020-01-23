package com.evnt.eve.modules.logic.extra;

import com.evnt.common.exceptions.FBException;
import com.evnt.eve.modules.ImplController;
import com.fbi.util.exception.ExceptionMainFree;
import com.unigrative.plugins.models.Seed;
import com.fbi.fbdata.misc.SeedFpo;
import com.fbi.entity.extra.SeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LogicSeed extends ImplController {

    @Autowired
    private SeedRepository seedRepository;

    @Transactional //TODO 19.10 change to FbTransactional
    public Seed updateSeed (final Seed seed) throws FBException{
        if (seed == null) {
            throw new ExceptionMainFree("Seed cannot be null");
        }
        if (seed.getCommonName() == null || seed.getCommonName().isEmpty()){
            throw new ExceptionMainFree("Seed common name cannot be null");
        }

        //TODO Date modified check for out of date error
//        if (terms.getId() > 0) {
//            final PaymentTermsFpo savedTerm = (PaymentTermsFpo)this.getPaymentTermsRepository().findById(terms.getId());
//            if (!DateUtil.isEqual(terms.getDateLastModified(), savedTerm.getDateLastModified())) {
//                throw new ExceptionMainFree(CodeObjectConst.PAYMENT_TERM, CodeErrorConst.CLIENT_OUT_OF_DATE, new Object[] { terms.getName() });
//            }
//        }

        SeedFpo seedFpo = this.convertSeed(seed);
        seedFpo = seedRepository.save(seedFpo);

        return this.toObject(seedFpo);
    }

    //TODO: GET LIST EXAMPLE
//    @FbReadOnlyTransactional
//    public ArrayList<CustomFieldFpo> getCustomFields(final RecordTypeConst recordType) {
//        if (recordType == null) {
//            return new ArrayList<CustomFieldFpo>();
//        }
//        return (ArrayList<CustomFieldFpo>)this.getCustomFieldRepository().findByTableId(recordType.getId());
//    }




    private SeedFpo convertSeed(Seed seed) {
        if (seed == null){
            return null;
        }
        SeedFpo seedFpo = new SeedFpo(); //TODO look for existing first
        seedFpo.setCommonName(seed.getCommonName());
        seedFpo.setScientificName(seed.getScientificName());
        return seedFpo;
    }

    private Seed toObject(final SeedFpo seedFpo) {
        if (seedFpo == null) {
            return null;
        }
        final Seed seed = new Seed();
        seed.setId(seedFpo.getId());
        seed.setCommonName(seedFpo.getCommonName());
        seed.setScientificName(seedFpo.getScientificName());
        return seed;
    }
}
