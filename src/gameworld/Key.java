package gameworld;

/**
 * A key that can be used to unlock Passages.
 *
 * @author Dylan
 *
 */
public class Key extends Item {

  /**
   * Creates a key that will unlock passage p.
   *
   */
  public Key() {
    super("Key", "A key to unlock a passage.", "assets/key.png");
  }

  @Override
  public boolean canPickup() {
    return true;
  }

  @Override
  public boolean use(Player p) {
    // Subject to change
    return false;
  }

}
