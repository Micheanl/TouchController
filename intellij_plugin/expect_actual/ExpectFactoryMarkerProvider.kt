package top.fifthlight.intellij.expectactual

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import org.jetbrains.uast.UClass
import org.jetbrains.uast.getUParentForIdentifier

class ExpectFactoryMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(elements: List<PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        for (element in elements) {
            val identifierOwner = getUParentForIdentifier(element) ?: continue
            val identifierClass = (identifierOwner as? UClass) ?: continue
            if (!identifierClass.isInterface) {
                continue
            }
            val parentInterface = (identifierClass.uastParent as? UClass) ?: continue
            if (!parentInterface.isInterface) {
                continue
            }

            identifierClass.findAnnotation("top.fifthlight.mergetools.api.ExpectFactory") ?: continue

            result.add(
                NavigationGutterIconBuilder.create(Assets.Expect)
                    .setTarget(parentInterface)
                    .setTooltipText("Go to parent class")
                    .setAlignment(GutterIconRenderer.Alignment.LEFT)
                    .createLineMarkerInfo(element)
            )
        }
    }
}
