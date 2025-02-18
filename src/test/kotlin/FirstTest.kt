import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.jBlist
import com.intellij.driver.sdk.ui.components.elements.popup
import com.intellij.driver.sdk.ui.present
import com.intellij.driver.sdk.ui.shouldBe
import com.intellij.driver.sdk.ui.xQuery
import com.intellij.driver.sdk.waitForIndicators
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

class FirstTest {
    @Test
    fun testSettings(){
        Starter.newContext(
            "testExample",
            TestCase(
                IdeProductProvider.IC,
                GitHubProject.fromGithub(branchName = "master",
                    repoRelativeUrl = "JetBrains/ij-perf-report-aggregator"))
                .withVersion("2024.2")
        ).runIdeWithDriver().useDriverAndCloseIde {
            waitForIndicators(1.minutes)
            ideFrame {
                x(xQuery { byVisibleText("Current File") }).click()
                val configurations = popup().jBlist(xQuery { contains(byVisibleText("Edit Configurations")) })
                configurations.shouldBe("Configuration list is not present", present)
                Assertions.assertTrue(configurations.rawItems.contains("backup-data"),
                    "Configurations list doesn't contain 'backup-data' item: ${configurations.rawItems}")
            }
        }
    }
}