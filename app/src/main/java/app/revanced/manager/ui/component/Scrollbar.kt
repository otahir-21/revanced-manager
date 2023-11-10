package app.revanced.manager.ui.component

import android.graphics.drawable.GradientDrawable.Orientation
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gigamole.composescrollbars.Scrollbars
import com.gigamole.composescrollbars.ScrollbarsState
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsStaticKnobType

@Composable
fun Scrollbar(scrollState: ScrollState, modifier: Modifier = Modifier) {
    Scrollbars(
        state = ScrollbarsState(
            ScrollbarsConfig(orientation = ScrollbarsOrientation.Vertical),
            ScrollbarsScrollType.Scroll(
                knobType = ScrollbarsStaticKnobType.Auto(),
                state = scrollState
            )
        ),
        modifier = modifier
    )
}