package com.github.jameshnsears.quoteunquote;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import com.github.jameshnsears.quoteunquote.database.DatabaseRepositoryDouble;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import java.util.List;

public class QuoteUnquoteModelDouble extends QuoteUnquoteModel {
    public QuoteUnquoteModelDouble() {
        context = getApplicationContext();
        databaseRepository = DatabaseRepositoryDouble.getInstance(context);
    }

    public int countPrevious(int widgetId) {
        return databaseRepository.countPreviousCriteria(widgetId, ContentSelection.ALL)
                + databaseRepository.countPreviousCriteria(widgetId, ContentSelection.AUTHOR)
                + databaseRepository.countPreviousCriteria(widgetId, ContentSelection.SEARCH);
    }

    public List<QuotationEntity> getAllQuotations() {
        return databaseRepository.getAllQuotations();
    }
}
